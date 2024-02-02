package byteback.syntax.sootup

import sootup.java.core.JavaSootMethod
import byteback.syntax.common.DeclarationLike
import byteback.syntax.common.Named
import byteback.syntax.bytecode.MethodLike
import byteback.syntax.bytecode.MemberLike
import byteback.syntax.bytecode.Signed
import byteback.syntax.sootup.Member
import byteback.syntax.sootup.Member.given
import byteback.syntax.sootup.signature.MethodSignature
import byteback.syntax.sootup.signature.MethodSignature.given
import byteback.syntax.sootup.Signature
import byteback.syntax.sootup.Signature.given

type Method = JavaSootMethod

object Method {
  given MethodLike[Method] with {}
  given Signed[Method, MethodSignature] with {
    extension (method: Method) {
      def signature: MethodSignature = {
        return method.getSignature()
      }
    }
  }
  given DeclarationLike[Method] with {}
  given Named[Method] with {
    extension (method: Method) {
      def name: String = {
        return method.getName()
      }
    }
  }
}
