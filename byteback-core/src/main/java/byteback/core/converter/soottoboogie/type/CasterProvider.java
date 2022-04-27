package byteback.core.converter.soottoboogie.type;

import byteback.core.converter.soottoboogie.Prelude;
import byteback.core.representation.soot.type.SootType;
import byteback.core.representation.soot.type.SootTypeVisitor;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.FunctionReference;
import java.util.function.Function;
import soot.BooleanType;
import soot.IntType;
import soot.Type;

public class CasterProvider extends SootTypeVisitor<Function<Expression, Expression>> {

	private Function<Expression, Expression> caster;

	private final SootType toType;

	public CasterProvider(final SootType toType) {
		this.toType = toType;
	}

	public void setCaster(final Function<Expression, Expression> caster) {
		this.caster = caster;
	}

	@Override
	public void caseBooleanType(final BooleanType fromType) {
		toType.apply(new SootTypeVisitor<>() {

			@Override
			public void caseIntType(final IntType toType) {
				setCaster((expression) -> {
					final FunctionReference casting = Prelude.instance().getIntCastingFunction().makeFunctionReference();
					casting.addArgument(expression);

					return casting;
				});
			}

			@Override
			public void caseDefault(final Type toType) {
				setCaster(Function.identity());
			}

		});
	}

	@Override
	public void caseDefault(final Type fromType) {
		setCaster(Function.identity());
	}

	@Override
	public Function<Expression, Expression> result() {
		return caster;
	}

}
