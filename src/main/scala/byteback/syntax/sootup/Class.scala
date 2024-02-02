package byteback.syntax.sootup

import sootup.java.core.JavaSootClass
import byteback.syntax.common.Named
import byteback.syntax.common.DeclarationLike
import byteback.syntax.bytecode.ClassLike
import byteback.syntax.bytecode.Signed
import byteback.syntax.common.Typed
import scala.collection.JavaConverters._
import scala.jdk.OptionConverters._
import byteback.syntax.bytecode.Modifiable
import byteback.syntax.sootup.Type
import byteback.syntax.sootup.Type.given
import byteback.syntax.sootup.Field
import byteback.syntax.sootup.Field.given
import byteback.syntax.sootup.Method
import byteback.syntax.sootup.Method.given
import byteback.syntax.sootup.Annotation
import byteback.syntax.sootup.Annotation.given
import byteback.syntax.sootup.`type`.ClassType
import byteback.syntax.sootup.`type`.ClassType.given
import byteback.syntax.sootup.Signature
import byteback.syntax.sootup.Signature.given

type Class = JavaSootClass

object Class {
  given ClassLike[Class, Field, Method, Annotation] with {
    extension (`class`: Class) {
      def fields: List[Field] = {
        return `class`.getFields().asScala.toList
      }
      def methods: List[Method] = {
        return `class`.getMethods().asScala.toList
      }
      def annotations: List[Annotation] = {
        return `class`.getAnnotations(None.toJava).asScala.toList
      }
    }
  }
  given Typed[Class, ClassType] with {
    extension (`class`: Class) {
      def `type`: ClassType = {
        return `class`.getType()
      }
    }
  }
  given Signed[Class, ClassType] with {
    extension (`class`: Class) {
      def signature: ClassType = {
        return `class`.getType()
      }
    }
  }
  given DeclarationLike[Class] with {}
  given Named[Class] with {
    extension (`class`: Class) {
      def name: String = {
        return `class`.getName()
      }
    }
  }
}
