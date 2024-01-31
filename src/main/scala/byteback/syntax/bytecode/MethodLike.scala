package byteback.syntax.bytecode

import byteback.syntax.bytecode.Annotatable
import byteback.syntax.bytecode.MemberLike

trait MethodLike[
    This,
    +Parent
](using
    MemberLike[This, Parent]
)
