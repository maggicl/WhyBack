package byteback.analysis;

import soot.tagkit.AnnotationElem;

public abstract class AnnotationElemSwitch<R> extends soot.util.annotations.AnnotationElemSwitch
		implements
			Visitor<AnnotationElem, R> {

	@Override
	public void caseDefault(final AnnotationElem elem) {
	}

	public R visit(final AnnotationElem elem) {
		elem.apply(this);

		return result();
	}

}
