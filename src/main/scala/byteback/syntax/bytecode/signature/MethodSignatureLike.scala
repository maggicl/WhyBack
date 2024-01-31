package byteback.syntax.bytecode.signature

import byteback.syntax.bytecode.TypeLike
import byteback.syntax.common.Named

trait MethodSignatureLike[
    This,
    +Parent,
    Type
](using
    MemberSignatureLike[This, Parent],
    Named[This],
    TypeLike[Type]
) {
  extension (value: This) {
    def argumentTypes: Iterable[Type]
    def returnType: Type
  }
}
