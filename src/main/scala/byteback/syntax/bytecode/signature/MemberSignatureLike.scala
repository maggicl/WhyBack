package byteback.syntax.bytecode.signature

import byteback.syntax.bytecode.SignatureLike

trait MemberSignatureLike[
    -This,
    +ClassSignature
](using
    ClassSignatureLike[ClassSignature]
) extends SignatureLike[This] {
  extension (value: This) {
    def declaringClassSignature: ClassSignature
  }
}
