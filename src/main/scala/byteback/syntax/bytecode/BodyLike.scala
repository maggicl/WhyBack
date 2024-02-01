package byteback.syntax.bytecode.member

import byteback.syntax.common.StatementLike

trait BodyLike[
    This
] {
  extension (value: This) {
    def statements[Statement: StatementLike]: Iterable[Statement]
  }
}
