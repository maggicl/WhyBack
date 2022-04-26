package byteback.core.representation.soot.annotation;

import byteback.core.representation.Visitor;
import soot.tagkit.AbstractAnnotationElemTypeSwitch;
import soot.tagkit.AnnotationElem;

public abstract class SootAnnotationElementVisitor<R> extends AbstractAnnotationElemTypeSwitch
		implements
			Visitor<AnnotationElem, R> {

	@Override
	public void defaultCase(final Object object) {
		caseDefault((AnnotationElem) object);
	}

}
