/**
 * RUN: %{byteback} -cp %{jar} -c %{class} -o %t.bpl
 */
package byteback.test.exceptions;

import java.io.FileReader;
import java.io.IOException;

import byteback.annotations.Contract.Return;

public class TryWithResources {

	public void tryWithResources() throws IOException {
		try (FileReader ac = new FileReader("")) {
			System.out.println();
		}
	}

	@Return
	public void tryWithResourcesFinally() throws IOException {
		try (FileReader ac = new FileReader("")) {
			System.out.println();
		} finally {
			System.out.println();
		} 
	}

}
