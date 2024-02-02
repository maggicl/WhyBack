package byteback.syntax.sootup

import byteback.syntax.common.TypeLike

export sootup.core.types.Type

object Type {
  given TypeLike[Type] with {}
}
