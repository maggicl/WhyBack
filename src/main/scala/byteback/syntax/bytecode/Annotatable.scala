package byteback.syntax.bytecode

import byteback.syntax.bytecode.`type`.ClassTypeLike

trait Annotatable[
    This,
    +Annotation,
    +Type
](using
    AnnotationLike[Annotation, Type]
) {
  extension (value: This) {
    def annotations: Iterable[Annotation]
  }
}
