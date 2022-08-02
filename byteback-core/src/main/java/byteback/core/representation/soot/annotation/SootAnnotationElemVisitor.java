package byteback.core.representation.soot.annotation;

import byteback.core.representation.Visitor;
import soot.tagkit.AnnotationElem;
import soot.util.annotations.AnnotationElemSwitch;

public abstract class SootAnnotationElemVisitor<R> extends AnnotationElemSwitch implements Visitor<AnnotationElem, R> {

	@Override
	public void defaultCase(final Object object) {
		caseDefault((AnnotationElem) object);
	}

	public R visit(final AnnotationElem elem) {
		elem.apply(this);

		return result();
	}

}
