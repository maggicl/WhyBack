package byteback.syntax.generic

import byteback.syntax.generic.statement.Statement

package declaration {

  sealed trait Declaration

  trait ExtendedDeclaration

  sealed trait Type extends Declaration

  trait TypeExtension extends Type

  case object BooleanType extends Type

  case object NaturalType extends Type

  case object RealType extends Type

  sealed trait Binding

  trait ExtendedBinding extends Binding

  sealed abstract class NamedBinding(
    name: String,
    typeAccess: Type
  )

  abstract class ExtendedNamedBinding(
    name: String,
    typeAccess: Type
  ) extends NamedBinding(name, typeAccess)

  case class LocalBinding(
    name: String,
    typeAccess: Type
  ) extends NamedBinding(name, typeAccess)

  case class FunctionDeclaration(
    name: String,
    argumentBindings: Seq[LocalBinding],
    returnType: Type,
    body: Seq[Statement]
  ) extends Declaration

}
