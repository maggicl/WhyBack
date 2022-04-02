package byteback.annotations;

import byteback.annotations.Contract.Prelude;
import byteback.annotations.Contract.Pure;

/**
 * Utilities to aid the formulation of complex boolean expressions.
 *
 * Note that being defined as static functions, none of these operations are
 * short-circuiting. For this reason, using them outside of ByteBack might not
 * be ideal.
 */
public interface Operator {

	/**
	 * Boolean implication.
	 * 
	 * @param a
	 *            Antecedent of the implication.
	 * @param b
	 *            Consequent of the implication.
	 * @return {@code true} if {@code a -> b}.
	 */
	@Pure
	@Prelude("implies")
	public static boolean implies(final boolean a, final boolean b) {
		return !a || b;
	}

	/**
	 * Boolean equivalence.
	 * 
	 * @param a
	 *            First statement.
	 * @param b
	 *            Second statement.
	 * @return {@code true} if {@code a <-> b}.
	 */
	@Pure
	@Prelude("iff")
	public static boolean iff(final boolean a, final boolean b) {
		return a == b;
	}

	/**
	 * Boolean AND.
	 *
	 * @param a
	 *            First operand.
	 * @param b
	 *            Second operand.
	 * @return {@code true} if {@code a && b}.
	 */
	@Pure
	@Prelude("and")
	public static boolean and(final boolean a, final boolean b) {
		return a && b;
	}

	/**
	 * Boolean OR.
	 *
	 * @param a
	 *            First operand.
	 * @param b
	 *            Second operand.
	 * @return {@code true} if {@code a || b}.
	 */
	@Pure
	@Prelude("or")
	public static boolean or(final boolean a, final boolean b) {
		return a || b;
	}

	/**
	 * Boolean NOT.
	 *
	 * @param a
	 *            Single operand.
	 * @return {@code true} if {@code !o}.
	 */
	@Pure
	@Prelude("not")
	public static boolean not(final boolean a) {
		return !a;
	}

	/**
	 * Object equality.
	 *
	 * @param a
	 *            First operand.
	 * @param b
	 *            Second operand.
	 * @return {@code true} if {@code a || b}.
	 */
	@Pure
	@Prelude("eq")
	public static boolean eq(final Object a, final Object b) {
		return a.equals(b);
	}

	@Pure
	@Prelude("eq")
	public static boolean eq(final boolean a, final boolean b) {
		return a == b;
	}

	@Pure
	@Prelude("eq")
	public static boolean eq(final byte a, final byte b) {
		return a == b;
	}

	@Pure
	@Prelude("eq")
	public static boolean eq(final int a, final int b) {
		return a == b;
	}

	@Pure
	@Prelude("eq")
	public static boolean eq(final char a, final char b) {
		return a == b;
	}

	@Pure
	@Prelude("eq")
	public static boolean eq(final double a, final double b) {
		return a == b;
	}

	@Pure
	@Prelude("eq")
	public static boolean eq(final float a, final float b) {
		return a == b;
	}

	@Pure
	@Prelude("eq")
	public static boolean eq(final long a, final long b) {
		return a == b;
	}

	@Pure
	@Prelude("eq")
	public static boolean eq(final short a, final short b) {
		return a == b;
	}

  /**
   * Numeric relational operations.
   */
	@Pure
	@Prelude("gt")
	public static boolean gt(final double a, final double b) {
		return a == b;
	}

	@Pure
	@Prelude("gt")
	public static boolean gt(final float a, final float b) {
		return a == b;
	}

	@Pure
	@Prelude("gt")
	public static boolean gt(final long a, final long b) {
		return a == b;
	}

	@Pure
	@Prelude("gt")
	public static boolean gt(final short a, final short b) {
		return a == b;
	}

	/**
	 * Specifies a reference to a old expression in a condition.
	 * <p>
	 * This expression is only allowed in the specification of conditions.
	 *
	 * @param a
	 *            Single operand.
	 * @return The given {@code a} instance.
	 */
	public static <T> T old(T a) {
		return a;
	}

}
