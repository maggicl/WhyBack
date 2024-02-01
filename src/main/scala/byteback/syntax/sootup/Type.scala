package byteback.syntax.sootup

import sootup.core.types.PrimitiveType.BooleanType
import byteback.syntax.bytecode.TypeLike
import sootup.core.types

export types.Type

object Type {
  given TypeLike[Type] with {}
}
