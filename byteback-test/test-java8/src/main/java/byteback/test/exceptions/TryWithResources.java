/**
 * RUN: %{byteback} -cp %{jar} -c %{class} -o %t.bpl
 */
package byteback.test.exceptions;

import java.io.IOException;

import static byteback.annotations.Contract.*;

public class TryWithResources {

	public class Resource implements AutoCloseable {
		private boolean closed;

		@Return
		public Resource() {
			closed = false;
		}

		@Ensure("isClosed")
		public void close() {
			closed = true;
		}

		@Pure
		@Predicate
		public boolean isClosed() {
			return closed;
		}

	}

	public void emptyTryWithResources() {
		try (Resource resource = new Resource()) {
		}
	}

	public void tryWithResourcesClosesResource() {
		Resource r = new Resource();

		try (Resource resource = r) {
		}

		assertion(r.isClosed());
	}

	public void emptyTryWithResourcesFinally() {
		try (Resource resource = new Resource()) {
		} finally {
		}

	}

	public void tryWithResourcesFinallyClosesResource() {
		Resource r = new Resource();

		try (Resource resource = r) {
		} finally {
			assertion(r.isClosed());
		}

	}

	public void throwingTryWithResourcesClosesResource() {
		Resource r = new Resource();

		try (Resource resource = r) {
			throw new RuntimeException();
		} finally {
			assertion(r.isClosed());
		}
	}

}

/**
 * RUN: %{verify} %t.bpl
 */
