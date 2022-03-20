package byteback.core.representation.soot.annotation;

import byteback.core.representation.Visitable;
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

    private final AnnotationElem sootAnnotationElement;

    public SootAnnotationElement(final AnnotationElem sootAnnotationElement) {
        this.sootAnnotationElement = sootAnnotationElement;
    }

    public String getName() {
        return sootAnnotationElement.getName();
    }

    public void apply(final SootAnnotationElementVisitor<?> visitor) {
        sootAnnotationElement.apply(visitor);
    }
    
}
