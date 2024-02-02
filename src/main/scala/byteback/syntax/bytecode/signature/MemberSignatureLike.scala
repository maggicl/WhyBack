package byteback.syntax.bytecode.signature

import byteback.syntax.bytecode.SignatureLike
import byteback.syntax.common.Typed

trait MemberSignatureLike[
    -This,
    +ParentSignature
](using
    SignatureLike[This],
    ClassSignatureLike[ParentSignature]
) {
  extension (value: This) {
    def declaringClassSignature: ParentSignature
  }
}
