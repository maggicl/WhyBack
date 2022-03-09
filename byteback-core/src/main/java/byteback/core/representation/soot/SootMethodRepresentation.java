package byteback.core.representation.soot;

import byteback.core.identifier.MethodName;
import byteback.core.representation.MethodRepresentation;
import byteback.core.visitor.type.soot.SootType;
import soot.Body;
import soot.SootMethod;
import soot.tagkit.AnnotationTag;
import soot.tagkit.VisibilityAnnotationTag;
import soot.tagkit.VisibilityParameterAnnotationTag;

import java.util.Collections;
import java.util.List;

public class SootMethodRepresentation implements MethodRepresentation {

    private final SootMethod sootMethod;

    private final MethodName name;

    /**
     * Constructor for the Soot method intermediate representation.
     *
     * @param sootMethod The wrapped {@code SootMethod} instance.
     */
    public SootMethodRepresentation(final SootMethod sootMethod) {
        this.sootMethod = sootMethod;
        this.name = new MethodName(sootMethod.getName());
    }

    @Override
    public MethodName getName() {
        return name;
    }

    public SootType getReturnType() {
        return new SootType(sootMethod.getReturnType());
    }

    public Body getBody() {
        return this.sootMethod.retrieveActiveBody();
    }

    public List<AnnotationTag> getAnnotations() {
        VisibilityAnnotationTag tag = (VisibilityAnnotationTag) sootMethod.getTag("VisibilityAnnotationTag");

        if (tag != null) {
            return tag.getAnnotations();
        } else {
            return Collections.emptyList();
        }
    }

    public List<VisibilityAnnotationTag> getParameterAnnotations() {
        VisibilityParameterAnnotationTag tag = (VisibilityParameterAnnotationTag) sootMethod.getTag("VisibilityParameterAnnotationTag");

        if (tag != null) {
            return tag.getVisibilityAnnotations();
        } else {
            return Collections.emptyList();
        }
    }

}
 
