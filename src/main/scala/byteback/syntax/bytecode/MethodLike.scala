package byteback.syntax.bytecode

import sootup.core.types.ClassType

trait MethodLike[
  -This,
  +Class,
  +ClassType,
  +Annotation
] extends Annotable[Annotation, Annotation, ClassType]
