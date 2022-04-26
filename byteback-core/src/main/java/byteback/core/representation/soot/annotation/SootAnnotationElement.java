package byteback.core.representation.soot.annotation;

import byteback.core.representation.Visitable;
import java.util.stream.Stream;
import soot.tagkit.AnnotationAnnotationElem;
import soot.tagkit.AnnotationArrayElem;
import soot.tagkit.AnnotationElem;
import soot.tagkit.AnnotationStringElem;

public class SootAnnotationElement implements Visitable<SootAnnotationElementVisitor<?>> {

	public static class StringElementExtractor extends SootAnnotationElementVisitor<String> {

		public String value;

		@Override
		public void caseAnnotationStringElem(final AnnotationStringElem element) {
			this.value = element.getValue();
		}

		@Override
		public void caseDefault(final AnnotationElem element) {
			System.out.println(element);
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

	private static void flatten(final Stream.Builder<SootAnnotationElement> builder, final AnnotationElem element) {
		element.apply(new SootAnnotationElementVisitor<>() {

			@Override
			public void caseAnnotationArrayElem(final AnnotationArrayElem element) {
				for (AnnotationElem value : element.getValues()) {
					flatten(builder, value);
				}
			}

			@Override
			public void caseAnnotationAnnotationElem(final AnnotationAnnotationElem element) {
				for (AnnotationElem value : element.getValue().getElems()) {
					flatten(builder, value);
				}
			}

			@Override
			public void caseDefault(final AnnotationElem $) {
				builder.add(new SootAnnotationElement(element));
			}

		});
	}

	private final AnnotationElem sootAnnotationElement;

	public SootAnnotationElement(final AnnotationElem sootAnnotationElement) {
		this.sootAnnotationElement = sootAnnotationElement;
	}

	public Stream<SootAnnotationElement> flatten() {
		final Stream.Builder<SootAnnotationElement> builder = Stream.builder();
		flatten(builder, sootAnnotationElement);

		return builder.build();
	}

	public String getName() {
		return sootAnnotationElement.getName();
	}

	public void apply(final SootAnnotationElementVisitor<?> visitor) {
		sootAnnotationElement.apply(visitor);
	}

}
