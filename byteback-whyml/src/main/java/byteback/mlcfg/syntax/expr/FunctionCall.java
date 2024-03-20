package byteback.mlcfg.syntax.expr;

import byteback.mlcfg.syntax.WhyFunctionParam;
import byteback.mlcfg.syntax.WhyFunctionSignature;
import byteback.mlcfg.syntax.types.WhyJVMType;
import byteback.mlcfg.syntax.types.WhyType;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FunctionCall implements Expression {
	private final WhyFunctionSignature function;
	private final List<Expression> params;

	public FunctionCall(WhyFunctionSignature function, List<Expression> params) {
		this.function = function;
		this.params = params;

		final List<WhyType> paramTypes = function.params().map(WhyFunctionParam::type).toList();

		if (paramTypes.size() != params.size()) {
			throw new IllegalArgumentException("expected %d arguments for %s, found %d".formatted(
					paramTypes.size(), function.name(), params.size()));
		}

		for (int i = 0; i < params.size(); i++) {
			final Expression argument = params.get(i);
			final WhyType paramType = paramTypes.get(i);

			if (!WhyType.compatible(paramType, argument.type())) {
				throw new IllegalArgumentException("argument %d must be of type %s, given %s".formatted(
						i + 1, paramType.getWhyType(), argument.type().getWhyType()));
			}
		}
	}

	@Override
	public String toWhy() {
		return Stream.concat(Stream.of(function.identifier()), params.stream().map(Expression::toWhy))
				.collect(Collectors.joining(" ", "(", ")"));
	}

	@Override
	public WhyJVMType type() {
		return function.returnType().jvm();
	}
}