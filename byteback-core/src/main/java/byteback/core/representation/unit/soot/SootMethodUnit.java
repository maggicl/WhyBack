package byteback.core.representation.unit.soot;

import soot.SootMethod;
import soot.Type;
import soot.tagkit.VisibilityAnnotationTag;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import byteback.core.representation.body.soot.SootBody;
import byteback.core.representation.type.soot.SootType;

public class SootMethodUnit {

    private final SootMethod sootMethod;

    /**
     * Constructor for the Soot method intermediate representation.
     *
     * @param classUnit The class unit corresponding to the method.
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
        return sootMethod.getParameterTypes().stream().map((type) -> new SootType(type)).collect(Collectors.toList());
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

    public SootMethod getSootMethod() {
        return sootMethod;
    }

    public Stream<SootAnnotation> annotations() {
        final VisibilityAnnotationTag tag = (VisibilityAnnotationTag) sootMethod.getTag("VisibilityAnnotationTag");

        if (tag != null) {
            return tag.getAnnotations().stream().map(SootAnnotation::new);
        } else {
            return Stream.empty();
        }
    }

    public Optional<SootAnnotation> getAnnotation(final String type) {
        return annotations().filter((tag) -> tag.getTypeName().equals(type)).findFirst();
    }

    public boolean hasAnnotation(final String type) {
        return getAnnotation(type).isPresent();
    }

}
