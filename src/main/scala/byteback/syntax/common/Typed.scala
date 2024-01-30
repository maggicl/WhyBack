package byteback.syntax.common

trait Typed[
    This,
    +Type
](using
    TypeLike[Type]
) {
  extension (value: This) {
    def `type`: Type
  }
}
