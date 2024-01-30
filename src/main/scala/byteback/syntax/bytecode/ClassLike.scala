package byteback.syntax.bytecode

import byteback.syntax.common.Typed

trait ClassLike[
    -This,
    +Field,
    +Method,
    +Annotation,
    +ClassType
](using
    MethodLike[Method, This, Annotation],
    FieldLike[Field, This, Annotation]
) extends Annotatable[This, Annotation]
    with Typed[This, ClassType] {
  extension (value: This) {
    def fields: Iterable[Field]
    def methods: Iterable[Method]
  }
}
