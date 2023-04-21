/**
 * RUN: %{byteback} -cp %{jar} -c %{class} -o %t.bpl
 */
package byteback.test.exceptions;

import java.io.FileReader;
import java.io.IOException;

import static byteback.annotations.Contract.*;

public class TryWithResources {

	public class Resource implements AutoCloseable {
		private boolean closed;

		@Return
		public Resource() {
			closed = false;
		}

		public void close() {
			closed = true;
		}

		@Pure
		public boolean isClosed() {
			return closed;
		}

	}

	public void emptyTryWithResources() {
		try (Resource resource = new Resource()) {
		}
	}

	public void tryWithResourcesClosesResource() throws IOException {
	}

	public void tryWithResourcesFinally() throws IOException {
		try (FileReader ac = new FileReader("")) {
		} finally {
		}

	}

}

/**
 * RUN: %{verify} %t.bpl
 */
