package byteback.syntax.sootup

import sootup.java.core.JavaSootField
import byteback.syntax.common.DeclarationLike
import byteback.syntax.common.Named
import byteback.syntax.bytecode.FieldLike
import byteback.syntax.sootup.Member
import byteback.syntax.sootup.Member.given

type Field = JavaSootField

object Field {
  given FieldLike[Field] with {}
  given DeclarationLike[Field] with {}
  given Named[Field] with {
    extension (field: Field) {
      def name: String = {
        return field.getName()
      }
    }
  }
}
