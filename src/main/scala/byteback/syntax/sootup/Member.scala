package byteback.syntax.sootup

import byteback.syntax.sootup.signature.MemberSignature
import sootup.core.model.SootClassMember
import byteback.syntax.bytecode.MemberLike
import byteback.syntax.sootup.`type`.ClassType
import byteback.syntax.sootup.`type`.ClassType.given
import sootup.core.signatures.SootClassMemberSignature

type Member[MemberSignature <: SootClassMemberSignature[?]] =
  SootClassMember[MemberSignature]

object Member {
  given MemberLike[Member[?], ClassType] with {
    extension (member: Member[?]) {
      def declaringClassSignature: ClassType = {
        return member.getDeclaringClassType()
      }
    }
  }
}
