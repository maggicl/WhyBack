package byteback.syntax.bytecode

import byteback.syntax.bytecode.Annotatable
import byteback.syntax.bytecode.MemberLike

trait MethodLike[
    -This,
    +Parent,
    +Type,
    +Annotation
] extends MemberLike[This, Parent]
    with Annotatable[This, Annotation]
