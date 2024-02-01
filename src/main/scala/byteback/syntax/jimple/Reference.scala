package byteback.syntax.jimple

import sootup.core.jimple.common.ref.ConcreteRef
import byteback.syntax.common.ReferenceLike
import byteback.syntax.jimple.Expression
import byteback.syntax.jimple.Expression.given
import byteback.syntax.common.ExpressionLike

type Reference = ConcreteRef

object Reference {
  given ReferenceLike[Reference] with {}
}
