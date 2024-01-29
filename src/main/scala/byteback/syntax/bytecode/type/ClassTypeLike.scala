package byteback.syntax.bytecode.`type`

import byteback.syntax.common.Named

trait ClassTypeLike[-This] extends Named[This] {
  extension (value: This) {
    def `package`: String
  }
}
