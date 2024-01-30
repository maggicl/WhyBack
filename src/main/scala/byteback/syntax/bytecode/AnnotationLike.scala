package byteback.syntax.bytecode

import byteback.syntax.common.Typed

trait AnnotationLike[
    -This,
    +ClassType
] extends Typed[This, ClassType]
