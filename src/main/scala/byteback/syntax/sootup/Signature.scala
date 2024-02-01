package byteback.syntax.sootup

import sootup.core.signatures
import byteback.syntax.bytecode.SignatureLike
import byteback.syntax.common.Named

export signatures.Signature

object Signature {
  given SignatureLike[Signature] with {}
  given Named[Signature] with {
    extension (signature: Signature) {
      def name: String = {
        signature.toString()
      }
    }
  }
}
