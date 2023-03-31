/**
 * RUN: %{byteback} -m -cp %{jar} -c %{class} -o %t.bpl
 */
package byteback.test.exceptions;

import byteback.annotations.Contract._;
import byteback.annotations.Special._;
import byteback.annotations.Operator._;
import byteback.annotations.Operator.{eq => equal};

class Basic {

  class ExceptionA extends Exception

  class ExceptionB extends Exception

  def tryCatchBlock(): Unit = {
    try {
      throw new Exception();
    } catch {
      case _: Exception =>
    }
  }

  @Return
  def neverThrows(): Unit = {
  }

  def neverCatches(): Unit = {
    try {
      neverThrows();
    } catch {
      case _: Exception => assertion(false);
    }
  }

  @Predicate
  def always_throws(e: Throwable): Boolean = {
    return e.isInstanceOf[Exception];
  }

  @Ensure("always_throws")
  def alwaysThrows(): Unit = {
    throw new Exception();
  }

  def alwaysCatches(): Unit = {
    try {
      alwaysThrows();
      assertion(false);
    } catch {
      case _: Throwable =>
    }
  }

}
/**
 * RUN: %{verify} %t.bpl | filecheck %s
 * CHECK: Boogie program verifier finished with 6 verified, 0 errors
 */
