package byteback.core.preprocessing;

import byteback.core.converter.soottoboogie.Namespace;
import byteback.core.converter.soottoboogie.method.procedure.DefinitionsCollector;
import byteback.core.representation.soot.body.SootExpressionVisitor;
import byteback.core.representation.soot.body.SootStatementVisitor;
import byteback.core.representation.soot.unit.SootMethods;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Stream;

import soot.Body;
import soot.Local;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.grimp.Grimp;
import soot.grimp.GrimpBody;
import soot.grimp.NewInvokeExpr;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InvokeExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.StaticFieldRef;
import soot.util.Chain;

public class BodyAggregator {

	final DefinitionsCollector defCollector;

	public BodyAggregator() {
		this.defCollector = new DefinitionsCollector();
	}

	public void removeUnusedLocals(final Body body) {
		final Chain<Local> locals = body.getLocals();
		final Iterator<Local> iterator = body.getLocals().snapshotIterator();

		defCollector.collect(body);

		while (iterator.hasNext()) {
			final Local local = iterator.next();

			if (defCollector.valueUsesOf(local).size() == 0 && defCollector.definitionsOf(local).size() == 0) {
				locals.remove(local);
			}
		}
	}

	public boolean hasSideEffects(final Value value) {

		final List<Value> uses = Stream.concat(value.getUseBoxes().stream()
				.map((useBox) -> useBox.getValue()), Stream.of(value))
				.toList();

		for (final Value use : uses) {
			final AtomicBoolean hasSideEffects = new AtomicBoolean(false);

			use.apply(new SootExpressionVisitor<>() {

				@Override
				public void caseInvokeExpr(final InvokeExpr invoke) {
					final SootMethod method = invoke.getMethod();
					hasSideEffects.set(!SootMethods.hasAnnotation(method, Namespace.PURE_ANNOTATION));
				}

				@Override
				public void caseNewExpr(final NewExpr newExpr) {
					hasSideEffects.set(true);
				}

				@Override
				public void caseNewArrayExpr(final NewArrayExpr newExpr) {
					hasSideEffects.set(true);
				}

				@Override
				public void caseNewInvokeExpr(final NewInvokeExpr invoke) {
					hasSideEffects.set(true);
				}

			});

			if (hasSideEffects.get()) {
				return true;
			}

		}

		return false;
	}

	public boolean assignsReference(final Local local) {
		for (final Unit use : defCollector.unitUsesOf(local)) {
			final AtomicBoolean impureAssignment = new AtomicBoolean(false);

			use.apply(new SootStatementVisitor<>() {

				@Override
				public void caseAssignStmt(final AssignStmt assignment) {
					final Value left = assignment.getLeftOp();

					left.apply(new SootExpressionVisitor<>() {

						@Override
						public void caseArrayRef(final ArrayRef reference) {
							impureAssignment.set(true);
						}

						@Override
						public void caseInstanceFieldRef(final InstanceFieldRef reference) {
							impureAssignment.set(true);
						}

						@Override
						public void caseStaticFieldRef(final StaticFieldRef reference) {
							impureAssignment.set(true);
						}

					});
				}

			});

			if (impureAssignment.get()) {
				return true;
			}
		}

		return false;
	}

	public GrimpBody transform(final Body source) {
		final GrimpBody body = Grimp.v().newBody(source, "gb");
		final Iterator<Unit> iterator = body.getUnits().snapshotIterator();
		defCollector.collect(body);

		while (iterator.hasNext()) {
			final Unit unit = iterator.next();

			for (ValueBox useBox : unit.getUseBoxes()) {
				final Value use = useBox.getValue();

				use.apply(new SootExpressionVisitor<>() {

					@Override
					public void caseLocal(final Local local) {
						final List<Unit> defs = defCollector.definitionsOfAt(local, unit);

						if (defs.size() == 1) {
							final Unit def = defs.iterator().next();

							def.apply(new SootStatementVisitor<>() {

								@Override
								public void caseAssignStmt(final AssignStmt assignment) {
									final Local assigned = local;
									final Value substitute = assignment.getRightOp();

									if (!hasSideEffects(substitute)
											&& (defCollector.hasSingleUse(assigned) || !assignsReference(assigned) )) {

										body.getUnits().remove(def);
										useBox.setValue(substitute);
									}

								}

								@Override
								public void caseDefault(final Unit statement) {
									throw new RuntimeException("Invalid assignment statement for local "
											+ local + " " + statement);
								}

							});
						}
					}

				});
			}
		}

		removeUnusedLocals(body);

		return new NewInvokeRemover().transform(body);
	}

}
