package byteback.core.converter.soot.boogie;

import byteback.core.representation.soot.body.SootExpression;
import byteback.core.representation.soot.body.SootExpressionVisitor;
import byteback.core.representation.soot.type.SootType;
import byteback.core.representation.soot.type.SootTypeVisitor;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.FunctionReference;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import soot.BooleanType;
import soot.IntType;
import soot.Type;
import soot.Value;

public class CastingExtractor extends SootExpressionVisitor<Expression> {

	public static Function<Expression, Expression> makeCaster(final SootExpression expression, final SootType type) {
		final AtomicReference<Function<Expression, Expression>> caster = new AtomicReference<>();
		type.apply(new SootTypeVisitor<>() {

			@Override
			public void caseIntType(final IntType to) {
				expression.getType().apply(new SootTypeVisitor<>() {

					@Override
					public void caseBooleanType(final BooleanType from) {
						caster.set((expression) -> {
							final FunctionReference caster = Prelude.getIntCaster();
							caster.addArgument(expression);

							return caster;
						});
					}

					@Override
					public void caseDefault(final Type from) {
						caster.set(Function.identity());
					}

				});
			}

			@Override
			public void caseDefault(final Type to) {
				caster.set(Function.identity());
			}

		});

		return caster.get();
	}

	final ExpressionExtractor extractor;

	public CastingExtractor() {
		this.extractor = new ExpressionExtractor() {

			@Override
			public Expression visit(final SootExpression expression, final SootType type) {
				final Function<Expression, Expression> caster = makeCaster(expression, type);

				return caster.apply(super.visit(expression, type));
			}

		};
	}

  public Expression visit(final SootExpression expression, final SootType type) {
    return extractor.visit(expression, type);
  }

	@Override
	public void caseDefault(final Value value) {
		value.apply(extractor);
	}

	@Override
	public Expression result() {
		return extractor.result();
	}

}
