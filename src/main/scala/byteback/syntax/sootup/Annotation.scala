package byteback.syntax.sootup

import sootup.java.core.AnnotationUsage
import byteback.syntax.bytecode.AnnotationLike
import byteback.syntax.common.Typed
import byteback.syntax.sootup.Type
import byteback.syntax.sootup.Type.given
import byteback.syntax.sootup.`type`.ClassType
import byteback.syntax.sootup.`type`.ClassType.given

type Annotation = AnnotationUsage

object Annotation {
  given AnnotationLike[Annotation, ClassType] with {}
  given Typed[Annotation, ClassType] with {
    extension (annotation: Annotation) {
      def `type`: ClassType = {
        return annotation.getAnnotation()
      }
    }
  }
}
