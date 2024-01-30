package byteback.syntax.sootup.`type`

import sootup.core.types
import byteback.syntax.bytecode.`type`.ClassTypeLike
import byteback.syntax.bytecode.signature.ClassSignatureLike

export types.ClassType

object ClassType {
  given ClassTypeLike[ClassType]
      with ClassSignatureLike[ClassType]
      with {
    extension (classType: ClassType) {
      def name: String = {
        return classType.getClassName()
      }
      def `package`: String = {
        return classType.getPackageName().getPackageName()
      }
    }
  }
}
