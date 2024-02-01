package byteback.syntax.sootup.`type`

import sootup.core.types
import byteback.syntax.bytecode.`type`.ReferenceTypeLike
import byteback.syntax.sootup.Type
import byteback.syntax.sootup.Type.given

export types.ReferenceType

object ReferenceType {
  given ReferenceTypeLike[ReferenceType] with {}
}
