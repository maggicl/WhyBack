package byteback.syntax.sootup

import sootup.core.types.PrimitiveType.BooleanType
import byteback.syntax.bytecode
import byteback.syntax.common
import sootup.core.types

export types.Type

object Type {
  given bytecode.TypeLike[Type] with {}
  given CommonTypeLike: common.TypeLike[Type] with {}
}
