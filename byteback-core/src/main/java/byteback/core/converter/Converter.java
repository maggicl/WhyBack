package byteback.core.converter;

public interface Converter<A, B> {

    B convert(A input);

}
