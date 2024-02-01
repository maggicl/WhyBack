package byteback.syntax.sootup

import byteback.syntax.sootup.signature.MemberSignature
import sootup.core.model.SootClassMember
import byteback.syntax.bytecode.MemberLike
import byteback.syntax.sootup.`type`.ClassType

type Member[MemberSignature] = SootClassMember[MemberSignature]

object Member {
  given MemberLike[Member[?]] with {
    extension (member: Member[?]) {
      def parentSignature: ClassType = {
        return member.getDeclaringClassType()
      }
    }
  }
}
