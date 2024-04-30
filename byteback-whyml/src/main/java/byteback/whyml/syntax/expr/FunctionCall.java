package byteback.whyml.syntax.expr;

import byteback.whyml.identifiers.Identifier;
import byteback.whyml.printer.SExpr;
import static byteback.whyml.printer.SExpr.prefix;
import static byteback.whyml.printer.SExpr.terminal;
import byteback.whyml.syntax.expr.transformer.ExpressionTransformer;
import byteback.whyml.syntax.expr.transformer.ExpressionVisitor;
import byteback.whyml.syntax.function.WhyLocal;
import byteback.whyml.syntax.function.WhyFunctionSignature;
import byteback.whyml.syntax.type.WhyJVMType;
import byteback.whyml.syntax.type.WhyType;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public record FunctionCall(Identifier.L name,
						   WhyFunctionSignature signature,
						   List<Expression> actualParams) implements Expression {

	public FunctionCall {
		final List<WhyType> paramTypes = signature.params().stream().map(WhyLocal::type).toList();

		if (paramTypes.size() != actualParams.size()) {
			throw new IllegalArgumentException("expected %d arguments for %s, found %d".formatted(
					paramTypes.size(), signature.name(), actualParams.size()));
		}

		for (int i = 0; i < actualParams.size(); i++) {
			final Expression argument = actualParams.get(i);
			final WhyType paramType = paramTypes.get(i);

			if (!WhyType.jvmCompatible(paramType, argument.type())) {
				throw new IllegalArgumentException("argument %d must be of type %s, given %s".formatted(
						i + 1, paramType.getWhyType(), argument.type().getWhyType()));
			}
		}
	}


	public List<Expression> actualParams() {
		return Collections.unmodifiableList(actualParams);
	}

	@Override
	public SExpr toWhy() {
		return prefix(name.toString(), Stream.concat(
				Stream.of(terminal(Identifier.Special.HEAP)),
				actualParams.stream().map(Expression::toWhy)));
	}

	@Override
	public WhyJVMType type() {
		return signature.returnType().jvm();
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
