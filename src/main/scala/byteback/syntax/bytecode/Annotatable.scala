package byteback.syntax.bytecode

trait Annotable[-This, +Annotation, +ClassType](
  using AnnotationLike[Annotation, ClassType]
) {
  extension (value: This) {
    def annotations: Iterable[Annotation]
  }
}
