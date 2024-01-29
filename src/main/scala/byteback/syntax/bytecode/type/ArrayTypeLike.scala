package byteback.syntax.bytecode.`type`

import byteback.syntax.common.TypeLike

trait ArrayTypeLike[-This, +Type](
  using TypeLike[This], TypeLike[Type]
) {
  extension (value: This) {
    def base: Type
    def dimension: Int
  }
}
