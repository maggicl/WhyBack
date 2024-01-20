package byteback.context.bytecode

import byteback.syntax.bytecode.declaration.ClassType
import byteback.syntax.bytecode.declaration.MethodDeclaration
import byteback.syntax.bytecode.declaration.Type

trait MethodProvider {

  def loadMethod(classType: ClassType, name: String, argumentTypes: Seq[Type], returnType: Type): MethodDeclaration

  def loadMethod(classType: ClassType, name: String, argumentTypes: Seq[Type]): MethodDeclaration

}
