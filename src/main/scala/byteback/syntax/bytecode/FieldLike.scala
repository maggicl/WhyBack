package byteback.syntax.bytecode

import byteback.syntax.common.Typed
import byteback.syntax.bytecode.Annotatable
import byteback.syntax.bytecode.MemberLike
import byteback.syntax.bytecode.`type`.ClassTypeLike
import byteback.syntax.bytecode.signature.ClassSignatureLike

trait FieldLike[
    -This
](using
    MemberLike[This, ?]
)
