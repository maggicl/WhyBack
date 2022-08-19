/**
 * RUN: %{byteback} -cp %{jar} -c %{class} -o %t.bpl
 */
package byteback.test.enums;
public enum Basic {
	APPLE, BANANA, ORANGE, GRAPES;
}
