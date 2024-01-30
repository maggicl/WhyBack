package byteback.syntax.bytecode

import byteback.syntax.common.Typed
import byteback.syntax.bytecode.Annotatable
import byteback.syntax.bytecode.MemberLike
import byteback.syntax.bytecode.`type`.ClassTypeLike

trait FieldLike[
    This,
    +Parent,
    +Type,
    +Annotation
] extends MemberLike[This, Parent]
    with Typed[This, Type]
    with Annotatable[This, Annotation]
