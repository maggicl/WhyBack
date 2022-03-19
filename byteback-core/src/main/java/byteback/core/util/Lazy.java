package byteback.core.util;

import java.util.function.Supplier;

public class Lazy<T> {

    public static <T> Lazy<T> from(final Supplier<T> supplier) {
        return new Lazy<>(supplier);
    }

    private final Supplier<T> supplier;

    private T value;

    private Lazy(final Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public synchronized T get() {
        if (value != null) {
            return value;
        } else {
            return initialize();
        }
    }

    private T initialize() {
        value = supplier.get();

        return value;
    }

}
