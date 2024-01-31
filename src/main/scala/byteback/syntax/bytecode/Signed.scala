package byteback.syntax.bytecode

trait Signed[
    This,
    +Signature
](using
    SignatureLike[Signature]
) {
  extension (value: This) {
    def signature: Signature
  }
}
