package byteback.syntax.bytecode.signature

import byteback.syntax.bytecode.TypeLike

trait MethodSignatureLike[-This, +ClassType, +ArgumentType](
  using TypeLike[ArgumentType]
) extends MemberSignatureLike[This, ClassType] {
  extension (value: This) {
    def argumentTypes: Iterable[ArgumentType]
  }
}
