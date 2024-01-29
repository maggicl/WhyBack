package byteback.syntax.sootup

import sootup.java.core.JavaSootMethod
import byteback.syntax.common

type Method = JavaSootMethod

object Method {
  given common.DeclarationLike[Method] with {
    extension (method: Method) {
      def name: String = {
        return method.getName()
      }
    }
  }
}
