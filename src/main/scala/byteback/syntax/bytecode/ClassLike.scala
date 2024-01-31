package byteback.syntax.bytecode

import byteback.syntax.common.Typed
import byteback.syntax.bytecode.signature.ClassSignatureLike

trait ClassLike[
    This,
    +Signature,
    +Field,
    +Method
](using
    Signed[This, Signature],
    ClassSignatureLike[Signature],
    FieldLike[Field, This],
    MethodLike[Method, This]
) {
  extension (value: This) {
    def fields: Iterable[Field]
    def methods: Iterable[Method]
  }
}
