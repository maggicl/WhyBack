package byteback.syntax.sootup.signature

import sootup.core.signatures
import byteback.syntax.bytecode.signature.MethodSignatureLike
import byteback.syntax.sootup.signature.MemberSignature
import byteback.syntax.sootup.signature.MemberSignature.given
import byteback.syntax.sootup.Type
import byteback.syntax.sootup.Type.given
import scala.collection.JavaConverters._

export signatures.MethodSignature

object MethodSignature {
  given MethodSignatureLike[MethodSignature, Type] with {
    extension (methodSignature: MethodSignature) {
      def argumentTypes: List[Type] = {
        return methodSignature.getParameterTypes().asScala.toList
      }
      def returnType: Type = {
        return methodSignature.getType()
      }
    }
  }
}
