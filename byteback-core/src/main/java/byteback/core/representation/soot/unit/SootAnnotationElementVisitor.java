package byteback.core.representation.soot.unit;

import byteback.core.representation.Visitor;
import soot.tagkit.AnnotationElem;
import soot.util.annotations.AnnotationElemSwitch;

public abstract class SootAnnotationElementVisitor<R> extends AnnotationElemSwitch implements Visitor<AnnotationElem, R> {

    @Override
    public void defaultCase(final Object object) {
        caseDefault((AnnotationElem) object);
    }

}
