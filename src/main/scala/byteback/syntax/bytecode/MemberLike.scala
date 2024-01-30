package byteback.syntax.bytecode

trait MemberLike[
    -This,
    +Parent
] {
  extension (value: This) {
    def parent: Parent
  }
}
