package byteback.syntax.bytecode.`type`

import byteback.syntax.common.TypeLike

trait ClassTypeLike[
    -This: TypeLike
] {
  extension (value: This) {
    def `package`: String
  }
}
