package byteback.syntax.jimple

import sootup.core.jimple.common.ref.ConcreteRef
import byteback.syntax.common.ReferenceLike
import byteback.syntax.sootup.`type`.ReferenceType
import byteback.syntax.sootup.`type`.ReferenceType.given
import byteback.syntax.jimple.Expression
import byteback.syntax.jimple.Expression.given

type Reference = ConcreteRef

object Reference {
  given ReferenceLike[Reference, ReferenceType] with {}
}
