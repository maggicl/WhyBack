package byteback.syntax.sootup.`type`

import sootup.core.types
import byteback.syntax.bytecode
import byteback.syntax.sootup.Type
import byteback.syntax.sootup.Type.given
import byteback.syntax.common.TypeLike
import byteback.syntax.bytecode.`type`.ArrayTypeLike

export types.ArrayType

object ArrayType {
  given TypeLike[ArrayType] with {}
  given ArrayTypeLike[ArrayType] with {
    extension (arrayType: ArrayType) {
      def base: Type = {
        return arrayType.getBaseType()
      }
      def dimension: Int = {
        return arrayType.getDimension()
      }
    }
  }
}
