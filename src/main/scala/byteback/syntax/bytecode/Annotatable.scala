package byteback.syntax.bytecode

import byteback.syntax.bytecode.`type`.ClassTypeLike

trait Annotatable[
    -This,
    +Annotation
](using
    AnnotationLike[Annotation, ?]
) {
  extension (value: This) {
    def annotations: Iterable[Annotation]
  }
}
