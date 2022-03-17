package byteback.core.util;

import java.util.Optional;
import java.util.function.Supplier;

public class Lazy<T> {

    public static <T> Lazy<T> from(final Supplier<T> supplier) {
        return new Lazy<T>(supplier);
    }

    private final Supplier<T> supplier;

    private Optional<T> value;

    private Lazy(final Supplier<T> supplier) {
        this.supplier = supplier;
        this.value = Optional.empty();
    }

    public synchronized T get() {
        return value.orElseGet(() -> initialize());
    }

    private T initialize() {
        final T result = supplier.get();
        value = Optional.of(result);

        return result;
    }

}
