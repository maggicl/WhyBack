/**
 * RUN: %{byteback} -cp %{jar} -c %{class} | tee %t.bpl
 */
package byteback.test.record;

public class Simple {

	public record Point(int x, int y) {

		@Override
		public String toString() {
			return null;
		}

		@Override
		public int hashCode() {
			return 0;
		}

	};

}
/**
 * RUN: %{verify} %t.bpl
 */
