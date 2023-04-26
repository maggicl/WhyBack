/**
 * RUN: %{byteback} -m -cp %{jar} -c %{class} -o %t.bpl
 */

package byteback.test.exceptions;

import byteback.annotations.Contract._;
import byteback.annotations.Special._;
import byteback.annotations.Operator._;
import byteback.annotations.Operator.{eq => equal};

// TODO
class PatternMatching {

}
/**
 * RUN: %{verify} %t.bpl
 */
