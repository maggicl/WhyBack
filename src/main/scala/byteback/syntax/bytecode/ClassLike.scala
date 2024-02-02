package byteback.syntax.bytecode

import byteback.syntax.common.Typed

trait ClassLike[
    -This,
    +Field,
    +Method,
    +Annotation
](using
    FieldLike[Field],
    MethodLike[Method],
    AnnotationLike[Annotation, ?],
    Typed[This, ?],
    Signed[This, ?]
) {
  extension (value: This) {
    def fields: Iterable[Field]
    def methods: Iterable[Method]
    def annotations: Iterable[Annotation]
  }
}
