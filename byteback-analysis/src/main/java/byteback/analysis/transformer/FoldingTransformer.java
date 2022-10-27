package byteback.analysis.transformer;

import byteback.analysis.JimpleStmtSwitch;
import byteback.analysis.JimpleValueSwitch;
import byteback.analysis.UseDefineChain;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.JimpleBody;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.Ref;

public class FoldingTransformer extends BodyTransformer {

	final UseDefineChain useDefineChain;

	public FoldingTransformer() {
		this.useDefineChain = new UseDefineChain();
	}

	@Override
	protected void internalTransform(final Body body, String phaseName, Map<String, String> options) {
		if (body instanceof JimpleBody jimpleBody) {
			transform(jimpleBody);
		} else {
			throw new IllegalArgumentException("Can transform only Jimple");
		}
	}

	public static boolean hasSideEffects(final Value value) {
		return value instanceof InvokeExpr || value instanceof NewExpr || value instanceof NewArrayExpr;
	}

	public boolean isAssignedToReference(final Local local) {
		for (final Unit use : useDefineChain.unitUsesOf(local)) {
			if (use instanceof AssignStmt assignUnit) {
				final Value left = assignUnit.getLeftOp();

				if (left instanceof Ref) {
					return true;
				}
			}
		}

		return false;
	}

	public void transformBody(final Body body) {
		final Iterator<Unit> iterator = body.getUnits().snapshotIterator();
		useDefineChain.collect(body);

		while (iterator.hasNext()) {
			final Unit unit = iterator.next();

			for (ValueBox useBox : unit.getUseBoxes()) {
				final Value useValue = useBox.getValue();

				useValue.apply(new JimpleValueSwitch<>() {

					@Override
					public void caseLocal(final Local local) {
						final List<Unit> definitions = useDefineChain.definitionsOfAt(local, unit);

						if (definitions.size() == 1) {
							final Unit definitionUnit = definitions.iterator().next();

							definitionUnit.apply(new JimpleStmtSwitch<>() {

								@Override
								public void caseAssignStmt(final AssignStmt assignment) {
									final Value substituteValue = assignment.getRightOp();

									if (!hasSideEffects(substituteValue)
											&& (useDefineChain.hasSingleUse(local) || !isAssignedToReference(local))) {
										body.getUnits().remove(definitionUnit);
										useBox.setValue(substituteValue);
									}

								}

							});
						}
					}

				});
			}
		}
	}

}
