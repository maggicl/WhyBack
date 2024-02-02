package byteback.syntax.bytecode.signature

import byteback.syntax.common.Typed
import byteback.syntax.common.TypeLike
import byteback.syntax.bytecode.SignatureLike

trait FieldSignatureLike[
    -This,
    +Type
](using
    TypeLike[Type],
    SignatureLike[This]
)
