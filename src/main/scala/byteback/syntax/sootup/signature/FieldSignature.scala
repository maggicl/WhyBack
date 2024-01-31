package byteback.syntax.sootup.signature

import sootup.core.signatures
import byteback.syntax.bytecode.signature.FieldSignatureLike
import byteback.syntax.bytecode.signature.MemberSignatureLike
import byteback.syntax.sootup.`type`.ClassType
import byteback.syntax.sootup.`type`.ClassType.given
import byteback.syntax.sootup.Type
import byteback.syntax.sootup.Type.given
import byteback.syntax.common.Typed
import byteback.syntax.sootup.signature.MemberSignature
import byteback.syntax.sootup.signature.MemberSignature.given
import sootup.core.signatures.FieldSubSignature
import sootup.core.signatures.SootClassMemberSubSignature

export signatures.FieldSignature

object FieldSignature {
  given Typed[FieldSignature, Type] with {
    extension (fieldSignature: FieldSignature) {
      def `type`: Type = {
        return fieldSignature.getType()
      }
    }
  }
  given (using MemberSignatureLike[MemberSignature[?], ClassType]): FieldSignatureLike[FieldSignature, ClassType, Type] with {}
}
