package byteback.syntax.common

trait Typed[
    This
] {
  extension (value: This) {
    def `type`[Type: TypeLike]: Type
  }
}
