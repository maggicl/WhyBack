package byteback.syntax.bytecode.member

import byteback.syntax.common.StatementLike

trait BodyLike[
    This,
    +Statement
](using
    StatementLike[Statement]
) {
  extension (value: This) {
    def statements: Iterable[Statement]
  }
}
