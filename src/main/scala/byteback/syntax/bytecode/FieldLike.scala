package byteback.syntax.bytecode

import byteback.syntax.common.Typed
import byteback.syntax.bytecode.`type`.ClassTypeLike

trait FieldLike[
    -This,
    +Class,
    +Annotation,
    +Type,
    +ClassType <: Type
] extends Typed[This, Type]
    with Annotatable[This, Annotation]
    with MemberLike[This, ClassType] {
  extension (value: This) {
    def annotations: Iterable[Annotation]
  }
}
