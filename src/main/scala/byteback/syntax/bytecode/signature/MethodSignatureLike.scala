package byteback.syntax.bytecode.signature

import byteback.syntax.bytecode.TypeLike
import byteback.syntax.common.Named

trait MethodSignatureLike[
    -This: MemberSignatureLike
] {
  extension [Type: TypeLike](value: This) {
    def argumentTypes: Iterable[Type]
    def returnType: Type
  }
}
