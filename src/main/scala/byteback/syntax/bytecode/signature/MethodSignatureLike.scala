package byteback.syntax.bytecode.signature

import byteback.syntax.bytecode.TypeLike

trait MethodSignatureLike[
    -This,
    +ClassSignature,
    +ArgumentType
](using
    TypeLike[ArgumentType]
) extends MemberSignatureLike[This, ClassSignature] {
  extension (value: This) {
    def argumentTypes: Iterable[ArgumentType]
  }
}
