package byteback.syntax.bytecode

trait MethodLike[
    -This,
    +Class,
    +Annotation,
    +Type,
    +ClassType <: Type
] extends Annotatable[This, Annotation]
