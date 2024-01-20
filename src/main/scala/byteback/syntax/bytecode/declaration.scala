package byteback.syntax.bytecode

import byteback.syntax.generic.{declaration => generic}
import byteback.syntax.generic.declaration.FunctionDeclaration
import sootup.core.signatures.MethodSignature
import byteback.syntax.generic.statement.Statement

package declaration {

  sealed trait Declaration extends generic.ExtendedDeclaration

  sealed trait Modifier

  sealed trait AccessModifier extends Modifier

  case object PublicModifier extends AccessModifier

  case object PrivateModifier extends AccessModifier

  case object StaticModifier extends Modifier

  case object AbstractModifier extends Modifier

  export generic.LocalBinding

  sealed trait Type extends generic.TypeExtension

  export generic.BooleanType

  case object ByteType extends Type

  case object ShortType extends Type

  case object IntType extends Type

  case object LongType extends Type

  case object FloatType extends Type

  case object DoubleType extends Type

  sealed trait ReferenceType extends Type

  case class ClassType(
    packageName: String,
    className: String
  ) extends ReferenceType

  case class ArrayType(
    base: Type,
    dimension: Int
  ) extends ReferenceType

  case class MethodDeclaration(
    name: String,
    modifier: Modifier,
    argumentBindings: Seq[LocalBinding],
    returnType: Type,
    body: Seq[Statement]
  ) extends Declaration

  case class FieldDeclaration(
    name: String,
    typeAccess: Type
  ) extends Declaration

  case class ClassDeclaration(
    name: String,
    methods: Seq[MethodDeclaration],
    fields: Seq[FieldDeclaration]
  ) extends Declaration

}
