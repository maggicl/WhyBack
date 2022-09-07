/**
 * RUN: %{byteback} -cp %{jar} -c %{class} -o %t.bpl
 */
package byteback.test.complete;

import byteback.annotations.Contract._;
import byteback.annotations.Special._;
import byteback.annotations.Operator._;
import byteback.annotations.Operator.{eq => equal};

class GCD {

  @Pure
  def gcd_recursive(a: Int, b: Int): Int = {
    return conditional(equal(a, b),
      a,
      conditional(gt(a, b),
        gcd_recursive(a - b, b),
        gcd_recursive(a, b - a)));
  }

  @Predicate
  def result_is_gcd(a: Int, b: Int, r: Int): Boolean = {
    return equal(r, gcd_recursive(a, b));
  }

  @Predicate
  def arguments_are_positive(a: Int, b: Int): Boolean = {
    return gt(a, 0) & gt(b, 0);
  }

  @Require("arguments_are_positive")
  @Ensure("result_is_gcd")
  def apply(a: Int, b: Int): Int = {
    var r: Int = a;
    var x: Int = b;

    while (r != x) {
			invariant(gt(r, 0) & gt(x, 0));
			invariant(equal(gcd_recursive(r, x), gcd_recursive(a, b)));

			if (r > x) {
				r = r - x;
			} else {
				x = x - r;
			}
    }

    return r;
  }

}
/**
 * RUN: %{verify} %t.bpl | filecheck %s
 * CHECK: Boogie program verifier finished with 2 verified, 0 errors
 */
