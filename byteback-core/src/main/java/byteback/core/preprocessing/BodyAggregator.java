package byteback.core.preprocessing;

import byteback.core.converter.soottoboogie.Namespace;
import byteback.core.converter.soottoboogie.method.procedure.DefinitionsCollector;
import byteback.core.representation.soot.body.SootExpressionVisitor;
import byteback.core.representation.soot.body.SootStatementVisitor;
import byteback.core.representation.soot.unit.SootMethods;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicBoolean;
import soot.Body;
import soot.Local;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.grimp.Grimp;
import soot.grimp.GrimpBody;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InvokeExpr;
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
		final AtomicBoolean hasSideEffects = new AtomicBoolean(false);

		value.apply(new SootExpressionVisitor<>() {

				@Override
				public void caseInvokeExpr(final InvokeExpr invoke) {
					final SootMethod method = invoke.getMethod();
					hasSideEffects.set(!SootMethods.hasAnnotation(method, Namespace.PURE_ANNOTATION));
				}

			});

		return hasSideEffects.get();
	}

	public boolean hasLoop(final Local local, final Unit unit) {
		final Stack<Local> next = new Stack<>();
		final HashSet<Local> visited = new HashSet<>();

		next.push(local);

		while (!next.isEmpty()) {
			final Local current = next.pop();
			final Set<Unit> uses = defCollector.unitUsesOf(current);
			visited.add(current);

			for (Unit use : uses) {
				final AtomicBoolean looping = new AtomicBoolean(false);

				use.apply(new SootStatementVisitor<>() {

					@Override
					public void caseAssignStmt(final AssignStmt assignment) {
						assignment.getLeftOp().apply(new SootExpressionVisitor<>() {

							@Override
							public void caseLocal(final Local value) {
								if (!visited.contains(local)) {
									next.push(local);
								} else if (local == value) {
									looping.set(true);
								}
							}

							@Override
							public void caseArrayRef(final ArrayRef reference) {
								looping.set(true);
							}

							@Override
							public void caseStaticFieldRef(final StaticFieldRef reference) {
								looping.set(true);
							}

							@Override
							public void caseInstanceFieldRef(final InstanceFieldRef reference) {
								looping.set(true);
							}

						});
					}

				});

				if (looping.get()) {
					return true;
				}
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

			for (final ValueBox box : unit.getUseBoxes()) {
				final Value value = box.getValue();

				value.apply(new SootExpressionVisitor<>() {

					@Override
					public void caseLocal(final Local local) {
						final Set<Unit> definitions = defCollector.definitionsOf(local);
						final Set<ValueBox> uses = defCollector.valueUsesOf(local);

						if (definitions.size() == 1 && !hasLoop(local, unit)) {
							final Unit definition = definitions.iterator().next();

							definition.apply(new SootStatementVisitor<>() {

								@Override
								public void caseAssignStmt(final AssignStmt assignment) {
									final Value substute = assignment.getRightOp();

									if (uses.size() == 1 || !hasSideEffects(substute)) {
										body.getUnits().remove(definition);
										box.setValue(substute);
									}
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
