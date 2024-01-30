package byteback.syntax.bytecode

import byteback.syntax.bytecode.`type`._
import byteback.syntax.bytecode.signature._
import byteback.syntax.bytecode.member._

trait ViewLike[
    -This,
    +Type,
    +Class,
    -ClassSignature,
    +Field,
    -FieldSignature,
    +Method,
    -MethodSignature,
    +Annotation
](using
    ClassLike[Class, ClassSignature, Type, Field, Method, Annotation],
    FieldLike[Field, ClassSignature, Type, Annotation],
    MethodLike[Method, ClassSignature, Type, Annotation],
) {
  extension (value: This) {
    def classes: Iterable[Class]
    def fetchClass(classSignature: ClassSignature): Option[Class]
    def fetchMethod(methodSignature: MethodSignature): Option[Method]
    def fetchField(fieldSignature: FieldSignature): Option[Field]
  }
}
