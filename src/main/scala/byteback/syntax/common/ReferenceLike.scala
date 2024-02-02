package byteback.syntax.common

import byteback.syntax.common.Typed
import byteback.syntax.bytecode.`type`.ClassTypeLike

trait ReferenceLike[
    -This,
    +Type
](using
    Typed[This, Type],
    TypeLike[Type]
)
