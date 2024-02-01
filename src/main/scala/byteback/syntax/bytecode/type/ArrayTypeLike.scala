package byteback.syntax.bytecode.`type`

import byteback.syntax.bytecode.TypeLike

trait ArrayTypeLike[
    This: TypeLike
] {
  extension (value: This) {
    def base[Type: TypeLike]: Type
    def dimension: Int
  }
}
