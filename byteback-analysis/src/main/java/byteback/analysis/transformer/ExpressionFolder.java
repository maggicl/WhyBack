package byteback.analysis.transformer;

import byteback.analysis.JimpleStmtSwitch;
import byteback.analysis.JimpleValueSwitch;
import byteback.analysis.Namespace;
import byteback.analysis.UseDefineChain;
import byteback.analysis.util.SootMethods;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.grimp.GrimpBody;
import soot.jimple.AssignStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;
import soot.jimple.Ref;

public class ExpressionFolder extends BodyTransformer {

	final UseDefineChain useDefineChain;

	public ExpressionFolder() {
		this.useDefineChain = new UseDefineChain();
	}

	@Override
	protected void internalTransform(final Body body, final String phaseName, final Map<String, String> options) {
		if (body instanceof GrimpBody) {
			transformBody(body);
		} else {
			throw new IllegalArgumentException("Can only transform Grimp");
		}
	}

	public static boolean isPure(final InvokeExpr invokeValue) {
		return SootMethods.hasAnnotation(invokeValue.getMethod(), Namespace.PURE_ANNOTATION)
			|| Namespace.isQuantifierClass(invokeValue.getMethod().getDeclaringClass());
	}

	public static boolean hasSideEffects(final Value value) {
		return (value instanceof InvokeExpr invokeValue && !isPure(invokeValue))
			|| value instanceof NewExpr || value instanceof NewArrayExpr;
	}

	public boolean isAssignedToReference(final Local local) {
		for (final Unit use : useDefineChain.unitUsesOf(local)) {
			if (use instanceof final AssignStmt assignUnit) {
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

									if (!hasSideEffects(substituteValue) && useDefineChain.hasSingleUse(local)) {
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
