package byteback.syntax.sootup.signature

import sootup.core.signatures
import sootup.core.signatures.SootClassMemberSubSignature
import byteback.syntax.bytecode.SignatureLike
import byteback.syntax.bytecode.signature.MemberSignatureLike
import byteback.syntax.sootup.Signature
import byteback.syntax.sootup.Signature.given
import byteback.syntax.sootup.`type`.ClassType
import byteback.syntax.sootup.`type`.ClassType.given
import sootup.core.signatures.SootClassMemberSignature

type MemberSignature[MemberSubSignature] = signatures.SootClassMemberSignature[MemberSubSignature]

object MemberSignature {
  given SignatureLike[MemberSignature[?]] with {
    extension (memberSignature: MemberSignature[?]) {
      def name: String = {
        return memberSignature.getName()
      }
    }
  }
  given MemberSignatureLike[MemberSignature[?]] with {
    extension (memberSignature: MemberSignature[?]) {
      def declaringClassSignature: ClassType = {
        return memberSignature.getDeclClassType()
      }
    }
  }
}
