package byteback.syntax.bytecode.signature

import byteback.syntax.common.Named
import byteback.syntax.common.Typed

trait FieldSignatureLike[
    This,
    +Parent,
    +Type
](using
    MemberSignatureLike[This, Parent],
    Named[This],
    Typed[This, Type]
)
