package byteback.syntax.bytecode

import sootup.core.types.ClassType

trait ClassLike[
  -This,
  +ClassType,
  +Method,
  +Field,
  +Annotation
](
  using MethodLike[Method, This, Annotation],
  FieldLike[This, This, ClassType, Annotation]
) extends Annotable[This, Annotation, ClassType]{
  extension (value: This) {
    def methods: Iterable[Method]
    def fields: Iterable[Field]
  }
}
