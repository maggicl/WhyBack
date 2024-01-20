package byteback.context.bytecode

import byteback.syntax.bytecode.declaration.ClassType
import byteback.syntax.bytecode.declaration.ClassDeclaration
import byteback.syntax.bytecode.declaration.FieldDeclaration

trait FieldProvider {

  def loadField(classType: ClassType, fieldName: String): FieldDeclaration

}
