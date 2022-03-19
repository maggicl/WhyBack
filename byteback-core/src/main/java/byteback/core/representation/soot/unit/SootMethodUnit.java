package byteback.core.representation.soot.unit;

import soot.SootMethod;
import soot.Type;
import soot.tagkit.VisibilityAnnotationTag;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import byteback.core.representation.soot.body.SootBody;
import byteback.core.representation.soot.type.SootType;

public class SootMethodUnit {

    private final SootMethod sootMethod;

    /**
     * Constructor for the Soot method intermediate representation.
     *
     * @param sootMethod The wrapped {@code SootMethod} instance.
     */
    public SootMethodUnit(final SootMethod sootMethod) {
        this.sootMethod = sootMethod;
    }

    public String getName() {
        return sootMethod.getName();
    }

    public String getIdentifier() {
        final StringBuilder builder = new StringBuilder();
        final Iterator<Type> typeIterator = sootMethod.getParameterTypes().iterator();
        builder.append(getName());
        builder.append("(");

        while (typeIterator.hasNext()) {
            builder.append(typeIterator.next().toString());

            if (typeIterator.hasNext()) {
                builder.append(",");
            }
        }

        builder.append(")");

        return builder.toString();
    }

    public List<SootType> getParameterTypes() {
        return sootMethod.getParameterTypes().stream().map(SootType::new).collect(Collectors.toList());
    }

    public SootType getReturnType() {
        return new SootType(sootMethod.getReturnType());
    }

    public SootBody getBody() {
        return new SootBody(sootMethod.retrieveActiveBody());
    }

    public SootClassUnit getClassUnit() {
        return new SootClassUnit(sootMethod.getDeclaringClass());
    }

    public Optional<SootAnnotation> getAnnotation(final String type) {
        return annotations().filter((tag) -> tag.getTypeName().equals(type)).findFirst();
    }

    public Stream<SootAnnotation> annotations() {
        final VisibilityAnnotationTag tag = (VisibilityAnnotationTag) sootMethod.getTag("VisibilityAnnotationTag");

        if (tag != null) {
            return tag.getAnnotations().stream().map(SootAnnotation::new);
        } else {
            return Stream.empty();
        }
    }

}
