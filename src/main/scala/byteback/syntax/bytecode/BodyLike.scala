package byteback.syntax.bytecode.member

trait BodyLike[
  -This,
  +Statement,
] {
  extension (value: This) {
    def statements: Iterable[Statement]
  }
}
