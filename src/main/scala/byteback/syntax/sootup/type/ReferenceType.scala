package byteback.syntax.sootup.`type`

import sootup.core.types
import byteback.syntax.bytecode.`type`.ReferenceTypeLike

export types.ReferenceType

object ReferenceType {
  given ReferenceTypeLike[ReferenceType] with {}
}
