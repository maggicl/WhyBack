package byteback.whyml.syntax;

import byteback.whyml.printer.Code;
import static byteback.whyml.printer.Code.block;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.expr.UnitLiteral;
import java.util.ArrayList;
import java.util.List;

public class BasicBlock {
	private final List<Expression> expressionList;

	public BasicBlock(List<Expression> expressionList) {
		this.expressionList = expressionList.isEmpty()
				? List.of(UnitLiteral.INSTANCE)
				: expressionList;
	}

	public Code toWhy() {
		final List<Code> lines = new ArrayList<>();
		for (int i = 0; i < expressionList.size() - 1; i++) {
			lines.add(expressionList.get(i).toWhy().statement("", ";"));
		}
		lines.add(expressionList.get(expressionList.size() - 1).toWhy().statement());

		return block(lines.stream());
	}
}
