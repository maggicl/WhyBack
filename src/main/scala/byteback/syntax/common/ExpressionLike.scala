package byteback.syntax.common

trait ExpressionLike[
    -This,
    +Type
](using
    Typed[This, Type]
)
