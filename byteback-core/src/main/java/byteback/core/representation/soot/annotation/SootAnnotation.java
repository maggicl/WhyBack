package byteback.core.representation.soot.annotation;

import java.util.Optional;
import java.util.stream.Stream;
import soot.tagkit.AnnotationTag;

public class SootAnnotation {

	private final AnnotationTag tag;

	public SootAnnotation(final AnnotationTag tag) {
		this.tag = tag;
	}

	public String getTypeName() {
		return tag.getType();
	}

	public Stream<SootAnnotationElement> elements() {
		return tag.getElems().stream().map(SootAnnotationElement::new);
	}

	public Stream<SootAnnotationElement> subelements() {
		return elements().flatMap(SootAnnotationElement::flatten);
	}

	public Optional<SootAnnotationElement> getValue() {
		return elements().filter((element) -> element.getName().equals("value")).findFirst();
	}

}
