package byteback.syntax.bytecode.signature

import byteback.syntax.common.Named
import byteback.syntax.common.TypeLike

trait MethodSignatureLike[
    -This,
    +Type
](using
    MemberSignatureLike[This, ?],
    TypeLike[Type]
) {
  extension (value: This) {
    def argumentTypes: Iterable[Type]
    def returnType: Type
  }
}
