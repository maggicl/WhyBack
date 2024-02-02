package byteback.syntax.jimple

import sootup.core.jimple.common.ref.ConcreteRef
import byteback.syntax.common.ReferenceLike
import byteback.syntax.jimple.Expression
import byteback.syntax.jimple.Expression.given
import byteback.syntax.common.ExpressionLike
import byteback.syntax.sootup.Type
import byteback.syntax.sootup.Type.given

type Reference = ConcreteRef

object Reference {
  given ReferenceLike[Reference, Type] with {}
}
