package byteback.syntax.bytecode

import byteback.syntax.common.Typed

trait ClassLike[
    This: Signed: Typed
] {
  extension (value: This) {
    def fields[Field: FieldLike]: Iterable[Field]
    def methods[Method: MethodLike]: Iterable[Method]
  }
}
