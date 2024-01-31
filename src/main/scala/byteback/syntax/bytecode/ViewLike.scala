package byteback.syntax.bytecode

import byteback.syntax.bytecode.`type`._
import byteback.syntax.bytecode.signature._
import byteback.syntax.bytecode.member._

trait ViewLike[
    This,
    +Class,
    -ClassSignature,
    +Field,
    -FieldSignature,
    +Method,
    -MethodSignature
](using
    ClassLike[Class, ClassSignature, Field, Method],
    FieldLike[Field, ClassSignature],
    MethodLike[Method, ClassSignature]
) {
  extension (value: This) {
    def classes: Iterable[Class]
    def fetchClass(classSignature: ClassSignature): Option[Class]
    def fetchMethod(methodSignature: MethodSignature): Option[Method]
    def fetchField(fieldSignature: FieldSignature): Option[Field]
  }
}
