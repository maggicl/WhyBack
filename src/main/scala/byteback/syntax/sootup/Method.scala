package byteback.syntax.sootup

import sootup.java.core.JavaSootMethod
import byteback.syntax.common.DeclarationLike
import byteback.syntax.common.Named
import byteback.syntax.bytecode.MethodLike
import byteback.syntax.bytecode.MemberLike
import byteback.syntax.bytecode.Signed
import byteback.syntax.sootup.Member
import byteback.syntax.sootup.Member.given
import sootup.core.signatures.MethodSignature

type Method = JavaSootMethod

object Method {
  given MethodLike[Method] with {}
  given Signed[Method] with {
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
