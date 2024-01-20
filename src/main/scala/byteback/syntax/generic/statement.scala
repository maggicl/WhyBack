package byteback.syntax.generic

import byteback.syntax.generic.expression.Reference
import byteback.syntax.generic.expression.Expression
import byteback.syntax.generic.expression.Call

package statement {

  sealed trait Statement

  trait ExtendedStatement

  case class Assign[R <: Reference](
    left: R,
    right: Expression
  ) extends Statement

  case class Label(
    name: String
  ) extends Statement

  case class Goto(
    name: String
  ) extends Statement

  case class If(
    condition: Expression,
    name: String
  ) extends Statement

  case class Invoke(
    call: Call
  ) extends Statement

  case class Return(
    expression: Expression,
  )

}
