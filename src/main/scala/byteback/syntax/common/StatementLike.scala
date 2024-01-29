package byteback.syntax.common

trait StatementLike[-This, +Reference](
  using ReferenceLike[Reference]
) {
  extension (value: This) {
    def uses: Iterable[Reference]
    def definitions: Iterable[Reference]
  }
}
