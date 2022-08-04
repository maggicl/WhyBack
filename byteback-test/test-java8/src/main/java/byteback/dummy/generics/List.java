package byteback.dummy.generics;

import static byteback.annotations.Contract.*;
import static byteback.annotations.Operator.*;
import byteback.annotations.Contract.Pure;

public class List<T> {

	private final T element;

	private final List<T> tail;

	public List(final T element, final List<T> tail) {
		this.element = element;
		this.tail = tail;
	}

	public List(final T element) {
		this(element, null);
	}

	public List<T> add(final T element) {
		return new List<T>(element, this);
	}

	@Pure
	public T getElement() {
		return element;
	}

	@Pure
	public List<T> getTail() {
		return tail;
	}

	public static void main() {
		final Object a = new Object();
		List<Object> l = new List<>(a);
		assertion(eq(l.getElement(), a));
	}

}
