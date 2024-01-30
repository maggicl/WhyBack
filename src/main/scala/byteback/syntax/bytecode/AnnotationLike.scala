package byteback.syntax.bytecode

import byteback.syntax.common.Typed
import byteback.syntax.bytecode.`type`.ClassTypeLike

trait AnnotationLike[
    This,
    +Type
](using
    ClassTypeLike[Type]
) extends Typed[This, Type]
