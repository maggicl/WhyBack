package byteback.syntax.common

trait ReferenceLike[
    This,
    +Type
] extends ExpressionLike[This, Type]
