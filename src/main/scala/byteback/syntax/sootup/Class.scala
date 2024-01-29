package byteback.syntax.sootup

import sootup.java.core.JavaSootClass
import byteback.syntax.common

type Class = JavaSootClass

object Class {
  given common.DeclarationLike[Class] with {
    extension (classModel: Class) {
      def name: String = {
        return classModel.getName()
      }
    }
  }
}
