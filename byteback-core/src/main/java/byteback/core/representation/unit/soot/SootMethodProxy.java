package byteback.core.representation.unit.soot;

import soot.Body;
import soot.SootMethod;
import soot.tagkit.AnnotationTag;
import soot.tagkit.VisibilityAnnotationTag;
import soot.tagkit.VisibilityParameterAnnotationTag;

import java.util.Collections;
import java.util.List;

public class SootMethodProxy {

    private final SootMethod sootMethod;

    /**
     * Constructor for the Soot method intermediate representation.
     *
     * @param sootMethod The wrapped {@code SootMethod} instance.
     */
    public SootMethodProxy(final SootMethod sootMethod) {
        this.sootMethod = sootMethod;
    }

    public String getName() {
        return sootMethod.getName();
    }

    public Body getBody() {
        return this.sootMethod.retrieveActiveBody();
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
        final VisibilityParameterAnnotationTag tag = (VisibilityParameterAnnotationTag) sootMethod.getTag("VisibilityParameterAnnotationTag");

        if (tag != null) {
            return tag.getVisibilityAnnotations();
        } else {
            return Collections.emptyList();
        }
    }

}
 
