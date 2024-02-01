package byteback.syntax.jimple

import sootup.core.jimple.basic.Value
import byteback.syntax.common.ExpressionLike
import byteback.syntax.common.Typed
import byteback.syntax.sootup.Type
import byteback.syntax.sootup.Type.given
import sootup.core.jimple.common.expr.JInstanceOfExpr

type Expression = Value

object Expression {
  given ExpressionLike[Expression] with {}
  given Typed[Expression] with {
    extension (expression: Expression) {
      def `type`: Type = {
        return expression.getType()
      }
    }
  }
}

