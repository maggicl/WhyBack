package byteback.context.bytecode

import byteback.syntax.bytecode.declaration.ClassType
import byteback.syntax.bytecode.declaration.ClassDeclaration

trait ClassProvider {

  def loadClass(classType: ClassType): ClassDeclaration

}
