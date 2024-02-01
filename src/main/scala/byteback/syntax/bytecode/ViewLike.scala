package byteback.syntax.bytecode

import byteback.syntax.bytecode.`type`._
import byteback.syntax.bytecode.signature._
import byteback.syntax.bytecode.member._

trait ViewLike[
    This
] {
  extension [
      Class: ClassLike,
      Method: MethodLike,
      Field: FieldLike
  ](value: This) {
    def classes: Iterable[Class]
    def fetchClass[Signature: SignatureLike](
        classSignature: Signature
    ): Option[Class]
    def fetchMethod[Signature: SignatureLike](
        methodSignature: Signature
    ): Option[Method]
    def fetchField[Signature: SignatureLike](
        fieldSignature: Signature
    ): Option[Field]
  }
}
