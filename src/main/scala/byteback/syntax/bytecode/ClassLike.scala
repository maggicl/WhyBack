package byteback.syntax.bytecode

import byteback.syntax.common.Typed
import byteback.syntax.bytecode.signature.ClassSignatureLike

trait ClassLike[
    -This,
    +Signature,
    +Type,
    +Field,
    +Method,
    +Annotation
](using
    ClassSignatureLike[Signature],
    MethodLike[Method, Signature, Type, Annotation],
    FieldLike[Field, Signature, Type, Annotation]
) extends Annotatable[This, Annotation]
    with Signed[This, Signature] {
  extension (value: This) {
    def fields: Iterable[Field]
    def methods: Iterable[Method]
  }
}
