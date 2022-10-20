package byteback.vimp.transformer;

import java.util.Map;

import byteback.core.converter.soottoboogie.Namespace;
import byteback.core.representation.soot.body.SootStatementVisitor;
import byteback.core.util.Lazy;
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

public class LogicUnitTransformer extends BodyTransformer implements StatementTransformer {

	private static final Lazy<LogicUnitTransformer> instance = Lazy.from(() -> new LogicUnitTransformer());

	public static LogicUnitTransformer v() {
		return instance.get();
	}

	private LogicUnitTransformer() {
	}

	@Override
	protected void internalTransform(final Body body, final String phaseName, final Map<String, String> options) {
		if (body instanceof JimpleBody jimpleBody) {
			internalTransform(jimpleBody);
		} else {
			throw new IllegalArgumentException("Can only transform Jimple");
		}
	}

	public void transformUnit(final UnitBox ubox) {
		final Unit unit = ubox.getUnit();

		unit.apply(new SootStatementVisitor<>() {

			@Override
			public void caseInvokeStmt(final InvokeStmt u) {
				final InvokeExpr value = u.getInvokeExpr();
				final SootMethod method = value.getMethod();
				final SootClass clazz = method.getDeclaringClass();

				if (Namespace.isContractClass(clazz)) {
					assert value.getArgCount() == 1;

					final Value argument = value.getArg(0);
					final LogicStmt newUnit;

					switch (method.getName()) {
						case Namespace.ASSERTION_NAME:
							newUnit = Vimp.v().newAssertionStmt(argument);
							break;
						case Namespace.ASSUMPTION_NAME:
							newUnit = Vimp.v().newAssumptionStmt(argument);
							break;
						case Namespace.INVARIANT_NAME:
							newUnit = Vimp.v().newInvariantStmt(argument);
							break;
						default:
							throw new IllegalStateException("Unknown logic statement " + method.getName());
					}

					putStatement(ubox, newUnit);
				}
			}

		});
	}

	private void internalTransform(final JimpleBody body) {
		for (final UnitBox ubox : body.getAllUnitBoxes()) {
			transformUnit(ubox);
		}
	}

}
