package byteback.syntax.bytecode.signature

import byteback.syntax.common.Named

trait FieldSignatureLike[-This, +ClassType] extends MemberSignatureLike[This, ClassType]
