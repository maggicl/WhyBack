package byteback.syntax.bytecode

import byteback.syntax.bytecode.signature.ClassSignatureLike

// TODO: Remove redundancy with MemberSignatureLike
trait MemberLike[
  -This,
  +ParentSignature
] (
  using ClassSignatureLike[ParentSignature]
){
  extension (value: This) {
    def declaringClassSignature: ParentSignature
  }
}
