package byteback.syntax.jimple

import sootup.core.jimple.basic.Value
import byteback.syntax.common.ExpressionLike
import byteback.syntax.common.Typed
import byteback.syntax.common.TypeLike
import byteback.syntax.sootup
import byteback.syntax.sootup.Type
import byteback.syntax.sootup.Type.given

type Expression = Value

object Expression {
  given ExpressionLike[Expression, Type] with {}
  given Typed[Expression, Type] with {
    extension (expression: Expression) {
      def `type`: Type = {
        return expression.getType()
      }
    }
  }
}
