package byteback.core.converter;

import byteback.core.representation.ClassRepresentation;

public interface Converter<A extends ClassRepresentation, B extends ClassRepresentation> {

    B convert(A input);

}
