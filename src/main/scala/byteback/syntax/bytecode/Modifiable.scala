package byteback.syntax.bytecode

trait Modifiable[
    This,
    +Modifier
](using
    ModifierLike[Modifier]
) {
  def modifiers: Iterable[Modifier]
}
