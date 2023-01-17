package byteback.util;

import java.util.Stack;

public class Stacks {

	public static <T> void pushAll(final Stack<T> stack, final Iterable<T> elements)  {
		for (final T element : elements) {
			stack.push(element);
		}
	}

	public static <T> void popAll(final Stack<T> stack, final Iterable<T> elements) {
		for (final T element : elements) {
			final T popped = stack.pop();
			assert popped == element;
		}
	}
}
