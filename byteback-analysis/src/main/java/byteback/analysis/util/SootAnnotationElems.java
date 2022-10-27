package byteback.analysis.util;

import byteback.analysis.AnnotationElemSwitch;
import soot.tagkit.AnnotationElem;
import soot.tagkit.AnnotationStringElem;

public class SootAnnotationElems {

	public static class StringElemExtractor extends AnnotationElemSwitch<String> {

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
			if (value == null) {
				throw new IllegalStateException("Cannot retrieve String value");
			} else {
				return value;
			}
		}

	}

}
