/**
 * RUN: %{byteback} -cp %{jar} -c %{class} --act --nct -o %t.bpl
 */
package byteback.test.examples;

import java.util.NoSuchElementException;

import byteback.annotations.Binding;
import byteback.annotations.Contract.Ensure;
import byteback.annotations.Contract.Predicate;
import byteback.annotations.Contract.Pure;
import byteback.annotations.Contract.Raise;
import byteback.annotations.Contract.Return;
import static byteback.annotations.Contract.*;
import static byteback.annotations.Operator.*;
import static byteback.annotations.Special.*;
import static byteback.annotations.Quantifier.*;

public class ReadResource {

	public static abstract class Resource implements AutoCloseable {

		public boolean isClosed;

		public boolean hasNext;

		@Return
		@Ensure("is_open")
		public Resource() {
			isClosed = false;
			hasNext = true;
		}

		@Predicate
		public boolean closes_only_this() {
			final Resource r = (Resource) Binding.reference();

			return forall(r, implies(neq(r, this), eq(old(r.isClosed), r.isClosed)));
		}

		@Return
		@Ensure("is_closed")
		@Ensure("closes_only_this")
		public void close() {
			isClosed = true;
		}

		@Pure
		@Predicate
		public boolean is_closed() {
			return isClosed;
		}

		@Pure
		@Predicate
		public boolean is_open() {
			return not(isClosed);
		}

		@Pure
		@Predicate
		public boolean has_next() {
			return hasNext;
		}

		@Pure
		@Predicate
		public boolean has_no_next() {
			return not(hasNext);
		}

		@Predicate
		public boolean reads_only_this(int returns) {
			final Resource r = (Resource) Binding.reference();

			return forall(r, implies(neq(r, this), eq(old(r.hasNext), r.hasNext)));
		}

		@Predicate
		public boolean is_open_and_has_next() {
			return not(isClosed) & hasNext;
		}

		@Raise(exception = IllegalStateException.class, when = "is_closed")
		@Raise(exception = NoSuchElementException.class, when = "has_no_next")
		@Ensure("reads_only_this")
		@Return(when = "is_open_and_has_next")
		public abstract int read();

	}

	@Pure
	@Predicate
	public static boolean a_or_r_is_null(Resource r, int[] a) {
		return eq(a, null) | eq(r, null);
	}

	@Pure
	@Predicate
	public static boolean r_and_a_are_not_null(Resource r, int[] a) {
		return neq(r, null) & neq(a, null);
	}

	@Pure
	@Predicate
	public static boolean r_is_open(Resource r, final int[] a) {
		return not(r.isClosed);
	}

	@Pure
	@Predicate
	public static boolean r_is_closed(final Resource r, final int[] a) {
		return r.isClosed;
	}

	public static void readInto(final Resource r, final int[] a) {
		try (r) {
			for (int i = 0; i < 10; ++i) {
				assertion(true);
			}
		} catch (IndexOutOfBoundsException | NoSuchElementException e) {
			return;
		}
	}

}
