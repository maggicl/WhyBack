package byteback.syntax.bytecode.signature

import byteback.syntax.bytecode.SignatureLike

trait MemberSignatureLike[
    This,
    +ClassSignature
](using
    SignatureLike[This],
    ClassSignatureLike[ClassSignature]
) {
  extension (value: This) {
    def declaringClassSignature: ClassSignature
  }
}
