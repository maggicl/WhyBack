package byteback.whyml.syntax.expr;

import byteback.whyml.identifiers.Identifier;
import byteback.whyml.printer.SExpr;
import static byteback.whyml.printer.SExpr.prefix;
import static byteback.whyml.printer.SExpr.terminal;
import byteback.whyml.syntax.expr.transformer.ExpressionTransformer;
import byteback.whyml.syntax.expr.transformer.ExpressionVisitor;
import byteback.whyml.syntax.function.WhyFunctionParam;
import byteback.whyml.syntax.function.WhyFunctionSignature;
import byteback.whyml.syntax.type.WhyJVMType;
import byteback.whyml.syntax.type.WhyType;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public record FunctionCall(WhyFunctionSignature function, Identifier.L name, List<Expression> params) implements Expression {

	public FunctionCall(WhyFunctionSignature function, Identifier.L name, List<Expression> params) {
		this.function = function;
		this.params = params;
		this.name = name;

		final List<WhyType> paramTypes = function.params().map(WhyFunctionParam::type).toList();

		if (paramTypes.size() != params.size()) {
			throw new IllegalArgumentException("expected %d arguments for %s, found %d".formatted(
					paramTypes.size(), function.vimp().name(), params.size()));
		}

		for (int i = 0; i < params.size(); i++) {
			final Expression argument = params.get(i);
			final WhyType paramType = paramTypes.get(i);

			if (!WhyType.jvmCompatible(paramType, argument.type())) {
				throw new IllegalArgumentException("argument %d must be of type %s, given %s".formatted(
						i + 1, paramType.getWhyType(), argument.type().getWhyType()));
			}
		}
	}


	@Override
	public List<Expression> params() {
		return Collections.unmodifiableList(params);
	}

	public Map<Identifier.L, Expression> argumentMap() {
		final List<Identifier.L> names = function.params().map(WhyFunctionParam::name).toList();

		return IntStream.range(0, names.size())
				.boxed()
				.collect(Collectors.toMap(names::get, params::get));
	}

	@Override
	public SExpr toWhy() {
		return prefix(name.toString(), Stream.concat(
				Stream.of(terminal(Identifier.Special.HEAP)),
				params.stream().map(Expression::toWhy)));
	}

	@Override
	public WhyJVMType type() {
		return function.returnType().jvm();
	}

	@Override
	public Expression accept(ExpressionTransformer transformer) {
		return transformer.transformFunctionCall(this);
	}

	@Override
	public void accept(ExpressionVisitor visitor) {
		visitor.visitFunctionCall(this);
	}
}
