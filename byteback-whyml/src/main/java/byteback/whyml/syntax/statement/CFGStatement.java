package byteback.whyml.syntax.statement;

import byteback.whyml.printer.Code;
import byteback.whyml.syntax.statement.visitor.StatementVisitor;

public interface CFGStatement {
	Code toWhy();

	void accept(StatementVisitor visitor);
}
