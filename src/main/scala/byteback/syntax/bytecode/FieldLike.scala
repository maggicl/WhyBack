package byteback.syntax.bytecode

import byteback.syntax.common.Typed
import sootup.core.types.ClassType

trait FieldLike[
  -This,
  +Class,
  +ClassType,
  +Annotation
] extends Typed[This, TypeLike[?]]
    with Annotable[This, Annotation, ClassType] {
  extension (value: This) {
    def annotations: Iterable[Annotation]
  }
}
