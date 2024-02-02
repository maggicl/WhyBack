package byteback.syntax.bytecode

import byteback.syntax.common.Typed
import byteback.syntax.bytecode.`type`.ClassTypeLike

trait AnnotationLike[
    -This,
    +Type
](using
    Typed[This, Type],
    ClassTypeLike[Type]
)
