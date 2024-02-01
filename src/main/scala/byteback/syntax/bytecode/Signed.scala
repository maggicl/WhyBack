package byteback.syntax.bytecode

trait Signed[
    This
] {
  extension (value: This) {
    def signature[Signature: SignatureLike]: Signature
  }
}
