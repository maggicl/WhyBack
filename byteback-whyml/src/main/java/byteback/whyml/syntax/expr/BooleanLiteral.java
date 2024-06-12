package byteback.whyml.syntax.expr;

import byteback.whyml.printer.SExpr;
import static byteback.whyml.printer.SExpr.terminal;
import byteback.whyml.syntax.expr.transformer.ExpressionTransformer;
import byteback.whyml.syntax.expr.transformer.ExpressionVisitor;
import byteback.whyml.syntax.type.WhyJVMType;
import java.util.Objects;

public final class BooleanLiteral implements Expression {
	public static final BooleanLiteral TRUE = new BooleanLiteral(true);
	public static final BooleanLiteral FALSE = new BooleanLiteral(false);

	private final boolean value;

	private BooleanLiteral(boolean value) {
		this.value = value;
	}

	public static BooleanLiteral of(boolean value) {
		return value ? TRUE : FALSE;
	}

	@Override
	public SExpr toWhy(boolean useLogicOps) {
		return terminal(value ? "true" : "false");
	}

	@Override
	public WhyJVMType type() {
		return WhyJVMType.BOOL;
	}

	@Override
	public Expression accept(ExpressionTransformer transformer) {
		return transformer.transformBooleanLiteral(this);
	}

	@Override
	public void accept(ExpressionVisitor visitor) {
		visitor.visitBooleanLiteral(this);
	}

	public boolean value() {
		return value;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (obj == null || obj.getClass() != this.getClass()) return false;
		var that = (BooleanLiteral) obj;
		return this.value == that.value;
	}

	@Override
	public int hashCode() {
		return Objects.hash(value);
	}

	@Override
	public String toString() {
		return "BooleanLiteral[" +
				"value=" + value + ']';
	}

}
