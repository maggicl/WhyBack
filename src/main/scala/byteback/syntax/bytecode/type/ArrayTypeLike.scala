package byteback.syntax.bytecode.`type`

import byteback.syntax.common.TypeLike

trait ArrayTypeLike[
    -This,
    +BaseType
](using
    TypeLike[This],
    TypeLike[BaseType]
) {
  extension (value: This) {
    def base: BaseType
    def dimension: Int
  }
}
