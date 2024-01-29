package byteback.syntax.bytecode.signature

import byteback.syntax.common.Named

trait MemberSignatureLike[-This, +ClassType] extends Named[This] {
  extension (value: This) {
    def declaringClassType: ClassType
  }
}
