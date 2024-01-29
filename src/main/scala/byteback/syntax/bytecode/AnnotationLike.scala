package byteback.syntax.bytecode

trait AnnotationLike[-This, +ClassType] {
  extension (value: This) {
    def `type`: ClassType
  }
}
