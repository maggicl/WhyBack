package byteback.syntax.bytecode

trait MemberLike[
    -This,
    +ClassType
] {
  extension (value: This) {
    def declaringClassType: ClassType
  }
}
