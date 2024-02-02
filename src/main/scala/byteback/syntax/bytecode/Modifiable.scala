package byteback.syntax.bytecode

trait Modifiable[
    -This
] {
  def modifiers[Modifier: ModifierLike]: Iterable[Modifier]
}
