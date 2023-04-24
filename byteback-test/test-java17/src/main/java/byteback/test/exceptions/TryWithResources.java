/**
 * RUN: %{byteback} -cp %{jar} -c %{class} -o %t.bpl
 */
package byteback.test.exceptions;

import java.io.IOException;

import static byteback.annotations.Contract.*;
import static byteback.annotations.Operator.*;

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

	public void tryWithResourcesOnExistingResourceClosesResource() {
		Resource resource = new Resource();

		try (resource) {
		}

		assertion(resource.isClosed());
	}

	public void tryWithResourcesFinallyOnExistingResourceClosesResource() {
		Resource resource = new Resource();

		try (resource) {
		} finally {
			assertion(resource.isClosed());
		}
	}

}

/**
 * RUN: %{verify} %t.bpl
 */
