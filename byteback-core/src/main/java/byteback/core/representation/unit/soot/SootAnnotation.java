package byteback.core.representation.unit.soot;

import java.nio.ByteBuffer;
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

    public Optional<ByteBuffer> getValue() {
        return Optional.of(ByteBuffer.wrap(tag.getValue()));
    }
    
}
