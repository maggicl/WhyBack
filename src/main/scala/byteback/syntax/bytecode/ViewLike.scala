package byteback.syntax.bytecode

import byteback.syntax.bytecode.`type`.ClassTypeLike
import byteback.syntax.bytecode.MethodLike
import byteback.syntax.bytecode.FieldLike

trait ViewLike[
  -This,
  +Type,
  +ClassType,
  +Method,
  +Field,
  +Annotation
](
  using TypeLike[Type],
  ClassTypeLike[ClassType],
  MethodLike[Method, ClassType, Annotation],
  FieldLike[Field, ClassType, Annotation],
  AnnotationLike[Annotation, ClassType]
)
