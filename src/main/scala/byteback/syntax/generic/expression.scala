package byteback.syntax.generic

import scala.util.Try
import scala.util.matching.Regex
import scala.util.Failure

package expression {

  sealed trait Expression

  trait ExtendedExpression extends Expression

  sealed abstract class Operation[T <: Operator](
    operator: T
  ) extends Expression

  abstract class ExtendedOperation[T <: Operator](
    operator: T
  ) extends Operation(operator)

  sealed abstract class Operator(
    string: String
  )

  abstract class ExtendedOperator(
    string: String
  ) extends Operator(string: String)

  abstract class BinaryOperation(
    operator: Operator,
    operands: Seq[Expression]
  ) extends Operation(operator)

  case class Addition(
    operands: Seq[Expression]
  ) extends BinaryOperation(Plus, operands)

  case class Subtraction(
    operands: Seq[Expression]
  ) extends BinaryOperation(Minus, operands)

  case class Division(
    operands: Seq[Expression]
  ) extends BinaryOperation(Quotient, operands)

  case class Modulo(
    operands: Seq[Expression]
  ) extends BinaryOperation(ModuloOperator, operands)

  case class Multiplication(
    operands: Seq[Expression]
  ) extends BinaryOperation(MultiplicationOperator, operands)

  case class Conjunction(
    operands: Seq[Expression]
  ) extends BinaryOperation(ConjunctionOperator, operands)

  case class Disjunction(
    operands: Seq[Expression]
  ) extends BinaryOperation(DisjunctionOperator, operands)

  sealed abstract class BinaryOperator(
    string: String
  ) extends Operator(string)

  abstract class ExtendedBinaryOperator(
    string: String
  ) extends BinaryOperator(string)

  case object Plus extends BinaryOperator("+")

  case object Minus extends BinaryOperator("-")

  case object Quotient extends BinaryOperator("/")

  case object ModuloOperator extends BinaryOperator("%")

  case object MultiplicationOperator extends BinaryOperator("*")

  case object ConjunctionOperator extends BinaryOperator("&&")

  case object DisjunctionOperator extends BinaryOperator("||")

  abstract class UnaryOperation(
    operator: UnaryOperator,
    operand: Expression
  )

  sealed abstract class UnaryOperator(
    string: String
  ) extends Operator(string)

  abstract class ExtendedUnaryOperator(
    string: String
  ) extends BinaryOperator(string)

  case object Negation extends UnaryOperator("!")

  sealed trait Call extends Expression

  trait ExtendedCall extends Call

  case class FunctionCall(
    name: String,
    parameters: Seq[Expression]
  ) extends Call

  sealed trait Terminal extends Expression

  trait ExtendedTerminal extends Terminal

  sealed trait Reference extends Terminal

  trait ExtendedReference extends Reference

  sealed abstract class NamedReference(
    name: String
  ) extends Reference

  abstract class ExtendedNamedReference(
    name: String
  ) extends NamedReference(name)

  case class LocalReference(
    name: String
  ) extends NamedReference(name)

  sealed trait Literal extends Terminal

  trait ExtendedLiteral extends Literal

  sealed trait ValueLiteral[T] extends Literal {
    def value: T
  }

  trait ExtendedValueLiteral[T] extends ValueLiteral[T]

  sealed abstract class BooleanLiteral(
    val value: Boolean
  ) extends ValueLiteral[Boolean]

  case object TrueLiteral extends BooleanLiteral(true)

  case object FalseLiteral extends BooleanLiteral(false)

  case class NaturalLiteral(
    value: BigInt
  ) extends ValueLiteral[BigInt]

  case class RealLiteral(
    value: BigDecimal
  ) extends ValueLiteral[BigDecimal]

}
