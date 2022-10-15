package byteback.vimp.transformer;

import java.util.Map;

import byteback.core.converter.soottoboogie.Namespace;
import byteback.core.representation.soot.body.SootStatementVisitor;
import byteback.vimp.Vimp;
import byteback.vimp.internal.LogicStmt;
import soot.Body;
import soot.BodyTransformer;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.UnitBox;
import soot.Value;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.JimpleBody;

public class LogicUnitTransformer extends BodyTransformer {

	@Override
	protected void internalTransform(final Body body, String phaseName, Map<String, String> options) {
		if (body instanceof JimpleBody jimpleBody) {
			internalTransform(jimpleBody);
		} else {
			throw new IllegalArgumentException("Can only transform Jimple");
		}
	}

	private void internalTransform(final JimpleBody body) {
		for (final UnitBox ubox : body.getAllUnitBoxes()) {
			final Unit unit = ubox.getUnit();

			unit.apply(new SootStatementVisitor<>() {

					@Override
					public void caseInvokeStmt(final InvokeStmt unit) {
						final InvokeExpr value = unit.getInvokeExpr();
						final SootMethod method = value.getMethod();
						final SootClass clazz = method.getDeclaringClass();

						if (Namespace.isContractClass(clazz)) {
							assert value.getArgCount() == 1;

							final Value argument = value.getArg(0);
							final LogicStmt substitute;

							switch (method.getName()) {
							case Namespace.ASSERTION_NAME:
								substitute = Vimp.v().newAssertionStmt(argument);
								break;
							case Namespace.ASSUMPTION_NAME:
								substitute = Vimp.v().newAssumptionStmt(argument);
								break;
							case Namespace.INVARIANT_NAME:
								substitute = Vimp.v().newInvariantStmt(argument);
								break;
							default:
								throw new IllegalStateException("Unknown logic statement " + method.getName());
							}

							ubox.setUnit(substitute);
						}
					}

			});
		}
	}

}
