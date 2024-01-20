package byteback.context.soot

import byteback.syntax.bytecode.declaration.ClassDeclaration
import byteback.syntax.soot.declaration.SootClassDeclaration
import byteback.syntax.bytecode.declaration.FieldDeclaration
import byteback.syntax.bytecode.declaration.MethodDeclaration
import byteback.syntax.bytecode.declaration.Type
import sootup.java.core.views.JavaView

class View(javaView: JavaView) {

  def loadClass(className: String): Option[ClassDeclaration] = {
    val classType = javaView.getIdentifierFactory().getClassType(className)
    val optionalJavaClass = javaView.getClass(classType)
    if (optionalJavaClass.isEmpty()) {
      return None
    } else {
      return Some(SootClassDeclaration(optionalJavaClass.get()))
    }
  }

  def loadField(fieldName: String): Option[FieldDeclaration] = {
    return None
  }

  def loadMethod(methodName: String, parameterTypes: Seq[Type], returnType: Type): Option[MethodDeclaration] = {
    return None
  }

}
