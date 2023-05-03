/**
 * RUN: %{byteback} -cp %{jar} -c %{class} --nct -o %t.bpl
 */

package byteback.test.exceptions;

import byteback.annotations.Contract._;
import byteback.annotations.Special._;
import byteback.annotations.Operator._;
import byteback.annotations.Operator.{eq => equal};

class A @Return () {

		@Return
		def method(): Unit = {}

}

class PotentialNullDereference {

		@Return
		def freshTarget(): Unit = {
				var a: A = new A()
				a.method()
		}

		@Return
		def passedTarget(a: A): Unit = {
				if (a != null) {
						a.method()
				}
		}

}

/**
 * RUN: %{verify} %t.bpl | filecheck %s
 * CHECK: Boogie program verifier finished with 5 verified, 0 errors
 */
