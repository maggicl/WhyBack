package byteback.syntax.bytecode

import byteback.syntax.common.Typed
import byteback.syntax.bytecode.Annotatable
import byteback.syntax.bytecode.MemberLike
import byteback.syntax.bytecode.`type`.ClassTypeLike

trait FieldLike[
    This,
    +Parent
](using
    MemberLike[This, Parent]
)
