package byteback.syntax.bytecode

import byteback.syntax.generic.{expression => generic}
import byteback.syntax.bytecode.declaration.ClassType

package expression {

  sealed trait BinaryOperator extends generic.ExtendedOperator

  case class BinaryOperation[T <: BinaryOperator](
    operator: T,
    operands: (Terminal, Terminal)
  ) extends generic.ExtendedOperation(operator)

  sealed trait UnaryOperator extends generic.ExtendedOperator

  case class UnaryOperation[T <: UnaryOperator](
    operator: T,
    operand: Terminal
  ) extends generic.ExtendedOperation(operator)

  sealed trait Terminal

  trait TerminalExtension extends Terminal

  sealed trait Literal extends Terminal

  trait LiteralExtension extends Literal

  export generic.BooleanLiteral

  sealed trait ValueLiteral[T] extends generic.ExtendedValueLiteral[T];

  case class ByteLiteral(
    value: Byte
  ) extends ValueLiteral[Byte]

  case class ShortLiteral(
    value: Short
  ) extends ValueLiteral[Short]

  case class IntLiteral(
    value: Int
  ) extends ValueLiteral[Int]

  case class LongLiteral(
    value: Long
  ) extends ValueLiteral[Long]

  case class FloatLiteral(
    value: Float
  ) extends ValueLiteral[Float]

  case class DoubleLiteral(
    value: Double
  ) extends ValueLiteral[Double]

  case class Reference(
    classType: ClassType
  ) extends Terminal

  abstract class Invoke(
    val base: Reference,
    val target: MethodSignature
  ) extends generic.ExtendedCall

  case class StaticInvoke(
    base: Reference,
    target: MethodSignature
  ) extends Invoke(base, target)

}
