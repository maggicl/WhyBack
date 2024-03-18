package byteback.mlcfg.syntax.expr;

import byteback.mlcfg.syntax.types.WhyType;

public sealed abstract class Expression permits BinaryExpression, BooleanLiteral, DoubleLiteral, FloatLiteral, NumericLiteral, UnaryExpression {
	public abstract String toWhy();

	public abstract WhyType type();
}
