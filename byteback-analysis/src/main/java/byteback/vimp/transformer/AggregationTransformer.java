package byteback.vimp.transformer;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import byteback.core.converter.soottoboogie.method.procedure.DefinitionsCollector;
import byteback.core.representation.soot.body.SootExpressionVisitor;
import byteback.core.representation.soot.body.SootStatementVisitor;
import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.grimp.NewInvokeExpr;
import soot.jimple.AssignStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.JimpleBody;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.Ref;

public class AggregationTransformer extends BodyTransformer {

	final DefinitionsCollector defCollector;

	public AggregationTransformer() {
		this.defCollector = new DefinitionsCollector();
	}

	@Override
	protected void internalTransform(final Body body, String phaseName, Map<String, String> options) {
		if (body instanceof JimpleBody jimpleBody) {
			internalTransform(jimpleBody);
		} else {
			throw new IllegalArgumentException("Can transform only Jimple");
		}
	}

	public boolean hasSideEffects(final Value value) {

		final List<Value> uses = Stream.concat(value.getUseBoxes().stream()
				.map((useBox) -> useBox.getValue()), Stream.of(value))
				.toList();

		for (final Value use : uses) {
			if (use instanceof InvokeExpr || use instanceof NewExpr
					|| use instanceof NewArrayExpr || use instanceof NewInvokeExpr) {
				return true;
			}
		}

		return false;
	}

	public boolean assignsReference(final Local local) {
		for (final Unit use : defCollector.unitUsesOf(local)) {
			if (use instanceof AssignStmt assignStmt) {
				final Value left = assignStmt.getLeftOp();

				if (left instanceof Ref) {
					return true;
				}
			}
		}

		return false;
	}

	protected void internalTransform(final JimpleBody body) {
		final Iterator<Unit> iterator = body.getUnits().snapshotIterator();
		defCollector.collect(body);

		while (iterator.hasNext()) {
			final Unit unit = iterator.next();

			for (ValueBox ubox : unit.getUseBoxes()) {
				final Value use = ubox.getValue();

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
											&& (defCollector.hasSingleUse(assigned) || !assignsReference(assigned))) {
										body.getUnits().remove(def);
										ubox.setValue(substitute);
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
	}
	
}
