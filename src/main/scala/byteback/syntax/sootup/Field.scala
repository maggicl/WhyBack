package byteback.syntax.sootup

import sootup.java.core.JavaSootField
import byteback.syntax.common

type Field = JavaSootField

object Field {
  given common.DeclarationLike[Field] with {
    extension (field: Field) {
      def name: String = {
        return field.getName()
      }
    }
  }
}
