package byteback.syntax.sootup

import sootup.core.types.PrimitiveType.BooleanType
import byteback.syntax.common.TypeLike
import sootup.core.types

export types.Type

object Type {
  given TypeLike[Type] with {}
}
