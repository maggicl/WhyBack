package byteback.analysis.util;

import byteback.analysis.AnnotationElemSwitch;
import soot.tagkit.AnnotationAnnotationElem;
import soot.tagkit.AnnotationArrayElem;
import soot.tagkit.AnnotationClassElem;
import soot.tagkit.AnnotationElem;
import soot.tagkit.AnnotationStringElem;

public sealed abstract class AnnotationElems<T> extends AnnotationElemSwitch<T> {

	@Override
	public final void caseAnnotationAnnotationElem(AnnotationAnnotationElem v) {
		for (final AnnotationElem elem : v.getValue().getElems()) {
			this.visit(elem);
		}
	}

	@Override
	public final void caseAnnotationArrayElem(AnnotationArrayElem v) {
		for (final AnnotationElem elem : v.getValues()) {
			this.visit(elem);
		}
	}

	public final static class StringElemExtractor extends AnnotationElems<String> {
		public String value;

		@Override
		public void caseAnnotationStringElem(final AnnotationStringElem element) {
			this.value = element.getValue();
		}

		@Override
		public void caseDefault(final AnnotationElem element) {
			throw new IllegalArgumentException("Expected annotation element of type string, got " + element);
		}

		@Override
		public String result() {
			return value;
		}
	}

	public final static class ClassElemExtractor extends AnnotationElems<String> {
		public String value;

		@Override
		public void caseAnnotationClassElem(final AnnotationClassElem element) {
			this.value = element.getDesc();
		}

		@Override
		public void caseDefault(final AnnotationElem element) {
			throw new IllegalArgumentException("Expected annotation element of type class, got " + element);
		}

		@Override
		public String result() {
			return value;
		}
	}
}
