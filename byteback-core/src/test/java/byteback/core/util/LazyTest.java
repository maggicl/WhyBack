package byteback.core.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class LazyTest {

	@Test
	public void Get_GivenInitializedValue_ReturnsSameInitializedValue() {
		final Object value = new Object();
		final Lazy<Object> lazyValue = Lazy.from(() -> value);
		assertEquals(lazyValue.get(), value);
	}

	@Test
	public void Get_CalledTwice_ReturnsSameValue() {
		final Lazy<Object> lazyValue = Lazy.from(() -> new Object());
		final Object first = lazyValue.get();
		final Object second = lazyValue.get();
		assertEquals(first, second);
	}

}
