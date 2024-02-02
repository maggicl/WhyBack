package byteback.syntax.sootup

export sootup.core.model.Modifier
export byteback.syntax.bytecode.ModifierLike

object Modifier {
  given ModifierLike[Modifier] with {}
}
