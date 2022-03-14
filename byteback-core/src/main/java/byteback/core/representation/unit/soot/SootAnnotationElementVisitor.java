package byteback.core.representation.unit.soot;

import byteback.core.representation.Visitor;
import soot.tagkit.AnnotationElem;
import soot.util.annotations.AnnotationElemSwitch;

public abstract class SootAnnotationElementVisitor<R> extends AnnotationElemSwitch implements Visitor<AnnotationElem, R> {
}
