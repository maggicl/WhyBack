package byteback.core.representation.unit.soot;

import java.util.Optional;

import soot.tagkit.AnnotationTag;

public class SootAnnotation {

    private final AnnotationTag tag;

    public SootAnnotation(AnnotationTag tag) {
        this.tag = tag;
    }

    public String getTypeName() {
        return tag.getType();
    }

    public Optional<String> getValue() {
        return Optional.of(new String(tag.getValue()));
    }
    
}
