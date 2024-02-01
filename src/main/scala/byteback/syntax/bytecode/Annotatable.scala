package byteback.syntax.bytecode

import byteback.syntax.bytecode.`type`.ClassTypeLike

trait Annotatable[
    This
] {
  extension (value: This) {
    def annotations[Annotation: AnnotationLike]: Iterable[Annotation]
  }
}
