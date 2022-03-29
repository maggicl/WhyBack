package byteback.core.converter.soot.boogie;

import byteback.core.representation.soot.type.SootType;
import byteback.core.representation.soot.type.SootTypeVisitor;
import byteback.frontend.boogie.ast.FunctionReference;
import soot.BooleanType;
import soot.IntType;
import soot.Local;
import soot.Type;

public class IntegerExpressionExtractor extends ExpressionExtractor {

	public IntegerExpressionExtractor() {
		super(new SootType(IntType.v()));
	}

	@Override
	public void caseLocal(final Local local) {
		super.caseLocal(local);
		final SootType type = new SootType(local.getType());

		type.apply(new SootTypeVisitor<>() {

			@Override
			public void caseBooleanType(final BooleanType booleanType) {
				final FunctionReference caster = Prelude.getIntCaster();
				caster.addArgument(operands.pop());
				pushExpression(caster);
			}

			@Override
			public void caseDefault(final Type type) {
				// No need to convert this local.
			}

		});

	}

}
