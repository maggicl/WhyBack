package byteback.syntax.bytecode.signature

import byteback.syntax.common.Named

trait FieldSignatureLike[
  -This,
  +ClassSignature
] extends MemberSignatureLike[This, ClassSignature]
