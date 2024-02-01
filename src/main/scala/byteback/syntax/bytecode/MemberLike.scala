package byteback.syntax.bytecode

import byteback.syntax.bytecode.signature.ClassSignatureLike

trait MemberLike[
    -This
] {
  extension (value: This) {
    def parentSignature[ParentSignature: ClassSignatureLike]: ParentSignature
  }
}
