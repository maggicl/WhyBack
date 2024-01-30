package byteback.syntax.bytecode

import byteback.syntax.bytecode.`type`._
import byteback.syntax.bytecode.signature._

trait ViewLike[
    -This,
    +Annotation,
    +Type,
    +Class,
    -ClassType,
    +Field,
    -FieldSignature,
    +Method,
    -MethodSignature
](using
    ClassLike[Class, Field, Method, Annotation, ClassType],
    FieldLike[Field, Class, Annotation],
    FieldSignatureLike[Field, Annotation],
    MethodLike[Method, Class, Annotation],
    MethodSignatureLike[MethodSignature, ClassType, Type],
    AnnotationLike[Annotation, ClassType],
    ClassTypeLike[ClassType]
) {
  extension (value: This) {
    def classes: Iterable[Class]
    def fetchClass(classtype: ClassType): Option[Class]
    def fetchMethod(methodSignature: MethodSignature): Option[Method]
    def fetchField(fieldSignature: FieldSignature): Option[Field]
  }
}
