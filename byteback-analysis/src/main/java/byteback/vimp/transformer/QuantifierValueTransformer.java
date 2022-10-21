package byteback.vimp.transformer;

import java.util.Map;

import byteback.core.converter.soottoboogie.Namespace;
import byteback.core.representation.soot.body.SootExpressionVisitor;
import byteback.core.util.Lazy;
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

public class QuantifierValueTransformer extends BodyTransformer implements ValueTransformer {

	private static final Lazy<QuantifierValueTransformer> instance = Lazy.from(QuantifierValueTransformer::new);

	public static QuantifierValueTransformer v() {
		return instance.get();
	}

	private QuantifierValueTransformer() {
	}

	@Override
	protected void internalTransform(final Body body, final String phaseName, final Map<String, String> options) {
		if (body instanceof JimpleBody jimpleBody) {
			internalTransform(jimpleBody);
		} else {
			throw new IllegalArgumentException("Can only transform Jimple");
		}
	}

	@Override
	public void transformValue(final ValueBox vbox) {
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
						case Namespace.UNIVERSAL_QUANTIFIER_NAME:
							substitute = Vimp.v().newLogicForallExpr(locals, expression);
							break;
						case Namespace.EXISTENTIAL_QUANTIFIER_NAME:
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
