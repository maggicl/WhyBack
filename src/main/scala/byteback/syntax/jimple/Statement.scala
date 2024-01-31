package byteback.syntax.jimple

import sootup.core.jimple.common.stmt.Stmt
import byteback.syntax.common.StatementLike
import byteback.syntax.sootup.Type
import byteback.syntax.jimple.Reference.given
import byteback.syntax.jimple.Expression.given
import scala.collection.JavaConverters._

type Statement = Stmt

object Statement {
  given StatementLike[Statement] with {
    extension (statement: Statement) {
      def uses: List[Reference] = {
        return statement
          .getUses()
          .asScala
          .toList
          .asInstanceOf[List[Reference]]
      }
      def definitions: List[Reference] = {
        return statement
          .getDefs()
          .asScala
          .toList
          .asInstanceOf[List[Reference]]
      }
    }
  }
}
