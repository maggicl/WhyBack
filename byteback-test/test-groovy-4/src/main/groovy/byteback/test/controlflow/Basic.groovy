/**
 * RUN: %{byteback} -cp %{jar} -c %{class} -o %t.bpl
 */
package byteback.test.controlflow

import static byteback.annotations.Contract.*;

class Basic {
}
/**
 * RUN: %{verify} %t.bpl
 */
