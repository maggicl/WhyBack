package byteback.syntax.common

trait Named[-This] {
  extension (value: This) {
    def name: String
  }
}
