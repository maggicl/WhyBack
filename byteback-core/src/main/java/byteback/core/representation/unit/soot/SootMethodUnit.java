package byteback.core.representation.unit.soot;

import soot.Body;
import soot.SootMethod;
import soot.Type;
import soot.tagkit.AnnotationTag;
import soot.tagkit.VisibilityAnnotationTag;
import soot.tagkit.VisibilityParameterAnnotationTag;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import byteback.core.representation.type.soot.SootType;

public class SootMethodUnit {

    private final SootClassUnit classUnit;

    private final SootMethod sootMethod;

    /**
     * Constructor for the Soot method intermediate representation.
     *
     * @param classUnit The class unit corresponding to the method.
     * @param sootMethod The wrapped {@code SootMethod} instance.
     */
    public SootMethodUnit(final SootClassUnit classUnit, final SootMethod sootMethod) {
        this.classUnit = classUnit;
        this.sootMethod = sootMethod;
    }

    public SootMethodUnit(final SootMethod sootMethod) {
        this(new SootClassUnit(sootMethod.getDeclaringClass()), sootMethod);
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

    public Body getBody() {
        return sootMethod.retrieveActiveBody();
    }

    public List<SootType> getParameterTypes() {
        return sootMethod.getParameterTypes().stream().map((type) -> new SootType(type)).collect(Collectors.toList());
    }

    public SootType getReturnType() {
        return new SootType(sootMethod.getReturnType());
    }

    public SootClassUnit getClassUnit() {
        return classUnit;
    }

    public SootMethod getSootMethod() {
        return sootMethod;
    }

    public List<AnnotationTag> getAnnotations() {
        final VisibilityAnnotationTag tag = (VisibilityAnnotationTag) sootMethod.getTag("VisibilityAnnotationTag");

        if (tag != null) {
            return tag.getAnnotations();
        } else {
            return Collections.emptyList();
        }
    }

    public List<VisibilityAnnotationTag> getParameterAnnotations() {
        final VisibilityParameterAnnotationTag tag = (VisibilityParameterAnnotationTag) sootMethod
                .getTag("VisibilityParameterAnnotationTag");

        if (tag != null) {
            return tag.getVisibilityAnnotations();
        } else {
            return Collections.emptyList();
        }
    }

}
