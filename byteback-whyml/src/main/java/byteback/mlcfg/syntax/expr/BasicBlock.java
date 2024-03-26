package byteback.mlcfg.syntax.expr;

import byteback.mlcfg.printer.Statement;
import static byteback.mlcfg.printer.Statement.block;
import static byteback.mlcfg.printer.Statement.line;
import java.util.ArrayList;
import java.util.List;

public class BasicBlock {
	private final List<Expression> expressionList;

	public BasicBlock(List<Expression> expressionList) {
		this.expressionList = expressionList.isEmpty()
				? List.of(UnitLiteral.INSTANCE)
				: expressionList;
	}

	public Statement toWhy() {
		final List<Statement> lines = new ArrayList<>();
		for (int i = 0; i < expressionList.size() - 1; i++) {
			lines.add(expressionList.get(i).toWhy().statement("", ";"));
		}
		lines.add(expressionList.get(expressionList.size() - 1).toWhy().statement());

		return block(lines.stream());
	}
}
