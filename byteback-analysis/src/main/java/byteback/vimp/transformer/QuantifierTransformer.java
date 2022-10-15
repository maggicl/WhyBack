package byteback.vimp.transformer;

import java.util.Map;

import byteback.core.converter.soottoboogie.Namespace;
import byteback.core.representation.soot.body.SootExpressionVisitor;
import byteback.vimp.Vimp;
import byteback.vimp.internal.QuantifierExpr;
import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.SootClass;
import soot.SootMethod;
import soot.Value;
import soot.ValueBox;
import soot.jimple.InvokeExpr;
import soot.jimple.JimpleBody;
import soot.util.Chain;
import soot.util.HashChain;

public class QuantifierTransformer extends BodyTransformer {

	public final String FORALL_NAME = Namespace.UNIVERSAL_QUANTIFIER_NAME;

	public final String EXISTS_NAME = Namespace.EXISTENTIAL_QUANTIFIER_NAME;

	@Override
	protected void internalTransform(final Body body, String phaseName, Map<String, String> options) {
		if (body instanceof JimpleBody jimpleBody) {
			internalTransform(jimpleBody);
		} else {
			throw new IllegalArgumentException("Can only transform Jimple");
		}
	}

	private void internalTransform(final JimpleBody body) {
		for (ValueBox vbox : body.getUseBoxes()) {
			final Value value = vbox.getValue();

			value.apply(new SootExpressionVisitor<>() {

				@Override
				public void caseInvokeExpr(final InvokeExpr value) {
					assert value.getArgCount() == 2;

					final SootMethod method = value.getMethod();
					final SootClass clazz = method.getDeclaringClass();

					if (Namespace.isQuantifierClass(clazz)) {
						final Chain<Local> locals = new HashChain<>();
						final Value expression;

						if (value.getArg(0) instanceof Local local) {
							locals.add(local);
							expression = value.getArg(1);
						} else {
							throw new RuntimeException("First argument of quantifier must be a local variable");
						}

						final QuantifierExpr substitute;

						switch (method.getName()) {
							case FORALL_NAME:
								substitute = Vimp.v().newLogicForallExpr(locals, expression);
								break;
							case EXISTS_NAME:
								substitute = Vimp.v().newLogicExistsExpr(locals, expression);
								break;
							default:
								throw new IllegalStateException("Unknown quantifier method " + method.getName());
						}

						vbox.setValue(substitute);
					}

				}

			});
		}
	}

}
