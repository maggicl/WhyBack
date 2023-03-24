/**
 * RUN: %{byteback} -cp %{jar} -c %{class} -o %t.bpl
 */
package byteback.test.exceptions;

import java.io.FileReader;
import java.io.IOException;

public class TryWithResources {

	public void test() throws IOException {
		try (FileReader ac = new FileReader("")) {
			System.out.println();
		}
	}

}
