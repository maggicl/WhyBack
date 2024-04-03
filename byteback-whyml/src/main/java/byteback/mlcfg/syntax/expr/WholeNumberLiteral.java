package byteback.mlcfg.syntax.expr;

import byteback.mlcfg.printer.SExpr;
import static byteback.mlcfg.printer.SExpr.terminal;
import byteback.mlcfg.syntax.expr.transformer.ExpressionTransformer;
import byteback.mlcfg.syntax.expr.transformer.ExpressionVisitor;
import byteback.mlcfg.syntax.types.WhyJVMType;
import java.util.Map;

public final class WholeNumberLiteral implements Expression {

	private static final Map<WhyJVMType, Map.Entry<Long, Long>> NARROWING_LIMITS = Map.of(
			WhyJVMType.BOOL, Map.entry((long) 0, (long) 1),
			WhyJVMType.BYTE, Map.entry((long) Byte.MIN_VALUE, (long) Byte.MAX_VALUE),
			WhyJVMType.SHORT, Map.entry((long) Short.MIN_VALUE, (long) Short.MAX_VALUE),
			WhyJVMType.CHAR, Map.entry((long) Character.MIN_VALUE, (long) Character.MAX_VALUE)
	);

	public static boolean isNarrowingTarget(WhyJVMType t) {
		return NARROWING_LIMITS.containsKey(t);
	}

	private final WhyJVMType type;
	private final long value;

	public WholeNumberLiteral(WhyJVMType type, long value) {
		if (!type.isWholeNumber()) {
			throw new IllegalArgumentException("literal has not valid numeric type: " + type);
		}

		this.type = type;
		this.value = value;
	}

	/**
	 * Transforms a literal of a wider type to a literal of a narrower type. This handles the cases where
	 * less-than-integer literals are used as integers in Soot, as the JVM represents all types narrower than int
	 * as ints.
	 * @param to the narrower literal type
	 * @return the narrower literal
	 */
	@Override
	public WholeNumberLiteral asType(WhyJVMType to) {
		if (to == this.type) return this;

		if (!NARROWING_LIMITS.containsKey(to)) throw new IllegalArgumentException("Cannot downsize to type " + to);
		if (this.type != WhyJVMType.INT) throw new IllegalArgumentException("Can downsize only from INT");

		if (this.value < NARROWING_LIMITS.get(to).getKey() || this.value > NARROWING_LIMITS.get(to).getValue()) {
			throw new IllegalArgumentException(this.value + "does not fit in " + to + " boundaries");
		}

		return new WholeNumberLiteral(to, this.value);
	}

	@Override
	public SExpr toWhy() {
		return terminal("(%d:%s)".formatted(value, type.getWhyType()));
	}

	@Override
	public WhyJVMType type() {
		return type;
	}

	@Override
	public Expression accept(ExpressionTransformer transformer) {
		return transformer.transformWholeNumberLiteral(this);
	}

	@Override
	public void accept(ExpressionVisitor visitor) {
		visitor.visitWholeNumberLiteral(this);
	}
}
