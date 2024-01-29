package byteback.syntax.sootup.signature

import sootup.core.signatures
import byteback.syntax.bytecode.signature.FieldSignatureLike
import byteback.syntax.sootup.`type`.ClassType
import sootup.core.types.ClassType

export signatures.FieldSignature

object FieldSignature {
  given FieldSignatureLike[FieldSignature, ClassType] with {
    extension (fieldSignature: FieldSignature) {
      def name: String = {
        return fieldSignature.getName()
      }
      def declaringClassType: ClassType = {
        return fieldSignature.getDeclClassType()
      }
    }
  }
}
