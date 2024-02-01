package byteback.syntax.bytecode

trait MemberLike[
    This
] {
  extension (value: This) {
    def parent[ParentSignature: SignatureLike]: ParentSignature
  }
}
