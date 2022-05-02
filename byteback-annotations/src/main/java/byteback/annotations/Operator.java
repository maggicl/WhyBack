package byteback.annotations;

import byteback.annotations.Contract.Prelude;
import byteback.annotations.Contract.Primitive;
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
	@Primitive
	@Prelude("~implies")
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
	@Primitive
	@Prelude("~iff")
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
	@Primitive
	@Prelude("~and")
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
	@Primitive
	@Prelude("~or")
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
	@Primitive
	@Prelude("~not")
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
	 * @return {@code true} if {@code a == b}.
	 */
	@Pure
	@Primitive
	@Prelude("~eq")
	public static boolean eq(final Object a, final Object b) {
		return a.equals(b);
	}

	@Pure
	@Primitive
	@Prelude("~eq")
	public static boolean eq(final boolean a, final boolean b) {
		return a == b;
	}

	@Pure
	@Primitive
	@Prelude("~eq")
	public static boolean eq(final byte a, final byte b) {
		return a == b;
	}

	@Pure
	@Primitive
	@Prelude("~eq")
	public static boolean eq(final int a, final int b) {
		return a == b;
	}

	@Pure
	@Primitive
	@Prelude("~eq")
	public static boolean eq(final char a, final char b) {
		return a == b;
	}

	@Pure
	@Primitive
	@Prelude("~eq")
	public static boolean eq(final double a, final double b) {
		return a == b;
	}

	@Pure
	@Primitive
	@Prelude("~eq")
	public static boolean eq(final float a, final float b) {
		return a == b;
	}

	@Pure
	@Primitive
	@Prelude("~eq")
	public static boolean eq(final long a, final long b) {
		return a == b;
	}

	@Pure
	@Primitive
	@Prelude("~eq")
	public static boolean eq(final short a, final short b) {
		return a == b;
	}

	/**
	 * Object inequality.
	 *
	 * @param a
	 *            First operand.
	 * @param b
	 *            Second operand.
	 * @return {@code true} if {@code a != b}.
	 */
	@Pure
	@Primitive
	@Prelude("~neq")
	public static boolean neq(final Object a, final Object b) {
		return a.equals(b);
	}

	@Pure
	@Primitive
	@Prelude("~neq")
	public static boolean neq(final boolean a, final boolean b) {
		return a == b;
	}

	@Pure
	@Primitive
	@Prelude("~neq")
	public static boolean neq(final byte a, final byte b) {
		return a == b;
	}

	@Pure
	@Primitive
	@Prelude("~neq")
	public static boolean neq(final int a, final int b) {
		return a == b;
	}

	@Pure
	@Primitive
	@Prelude("~neq")
	public static boolean neq(final char a, final char b) {
		return a == b;
	}

	@Pure
	@Primitive
	@Prelude("~neq")
	public static boolean neq(final double a, final double b) {
		return a == b;
	}

	@Pure
	@Primitive
	@Prelude("~neq")
	public static boolean neq(final float a, final float b) {
		return a == b;
	}

	@Pure
	@Primitive
	@Prelude("~neq")
	public static boolean neq(final long a, final long b) {
		return a == b;
	}

	@Pure
	@Primitive
	@Prelude("~neq")
	public static boolean neq(final short a, final short b) {
		return a == b;
	}

	/**
	 * Numeric relational operations.
	 */
	@Pure
	@Primitive
	@Prelude("~real.lt")
	public static boolean lt(final double a, final double b) {
		return a < b;
	}

	@Pure
	@Primitive
	@Prelude("~real.lt")
	public static boolean lt(final float a, final float b) {
		return a < b;
	}

	@Pure
	@Primitive
	@Prelude("~int.lt")
	public static boolean lt(final long a, final long b) {
		return a < b;
	}

	@Pure
	@Primitive
	@Prelude("~int.lt")
	public static boolean lt(final short a, final short b) {
		return a < b;
	}

	@Pure
	@Primitive
	@Prelude("~real.lte")
	public static boolean lte(final double a, final double b) {
		return a <= b;
	}

	@Pure
	@Primitive
	@Prelude("~real.lte")
	public static boolean lte(final float a, final float b) {
		return a <= b;
	}

	@Pure
	@Primitive
	@Prelude("~int.lte")
	public static boolean lte(final long a, final long b) {
		return a < b;
	}

	@Pure
	@Primitive
	@Prelude("~int.lte")
	public static boolean lte(final short a, final short b) {
		return a < b;
	}

	@Pure
	@Primitive
	@Prelude("~real.gte")
	public static boolean gte(final double a, final double b) {
		return a < b;
	}

	@Pure
	@Primitive
	@Prelude("~real.gte")
	public static boolean gte(final float a, final float b) {
		return a < b;
	}

	@Pure
	@Primitive
	@Prelude("~int.gte")
	public static boolean gte(final long a, final long b) {
		return a < b;
	}

	@Pure
	@Primitive
	@Prelude("~int.gte")
	public static boolean gte(final short a, final short b) {
		return a < b;
	}

	@Pure
	@Primitive
	@Prelude("~real.gt")
	public static boolean gt(final double a, final double b) {
		return a > b;
	}

	@Pure
	@Primitive
	@Prelude("~real.gt")
	public static boolean gt(final float a, final float b) {
		return a > b;
	}

	@Pure
	@Primitive
	@Prelude("~int.gt")
	public static boolean gt(final int a, final int b) {
		return a > b;
	}

	@Pure
	@Primitive
	@Prelude("~int.gt")
	public static boolean gt(final short a, final short b) {
		return a > b;
	}

	@Pure
	@Primitive
	@Prelude("~int.gt")
	public static boolean gt(final long a, final long b) {
		return a > b;
	}

}
