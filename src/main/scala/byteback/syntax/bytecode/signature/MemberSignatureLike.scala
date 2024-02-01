package byteback.syntax.bytecode.signature

import byteback.syntax.bytecode.SignatureLike
import byteback.syntax.common.Typed

trait MemberSignatureLike[
    -This: SignatureLike
] {
  extension (value: This) {
    def declaringClassSignature[Signature: ClassSignatureLike]: Signature
  }
}
