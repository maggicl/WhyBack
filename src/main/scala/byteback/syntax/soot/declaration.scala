package byteback.syntax.soot

import sootup.java.core.JavaSootField
import byteback.syntax.bytecode.declaration.FieldDeclaration
import sootup.java.core.JavaSootMethod
import byteback.syntax.bytecode.declaration.MethodDeclaration
import sootup.java.core.JavaSootClass
import byteback.syntax.bytecode.declaration.ClassDeclaration

package declaration {

  trait SootFieldDeclaration {

    val sootField: JavaSootField

  }

  object SootFieldDeclaration {

    def apply(javaField: JavaSootField): FieldDeclaration =
      new FieldDeclaration(
        name = javaField.getName(),
        typeAccess = null
      ) with SootFieldDeclaration {
        val sootField = javaField
      }

  }

  trait SootMethodDeclaration {

    val sootMethod: JavaSootMethod

  }

  object SootMethodDeclaration {

    def apply(javaMethod: JavaSootMethod): MethodDeclaration =
      new MethodDeclaration(
        name = javaMethod.getName(),
        signature = null,
        body = null
      ) with SootMethodDeclaration {
        val sootMethod = javaMethod
      }

  }

  trait SootClassDeclaration {

    val sootClass: JavaSootClass

  }

  object SootClassDeclaration {

    def apply(javaClass: JavaSootClass): ClassDeclaration =
      new ClassDeclaration(
        name = javaClass.getName(),
        methods = Seq[MethodDeclaration](),
        fields = Seq[FieldDeclaration]()
      ) with SootClassDeclaration {
        val sootClass = javaClass
      }
  }

}
