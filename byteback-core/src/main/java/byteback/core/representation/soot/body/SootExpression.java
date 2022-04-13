package byteback.core.representation.soot.body;

import byteback.core.representation.Visitable;
import byteback.core.representation.soot.type.SootType;
import java.util.List;
import soot.Value;
import soot.ValueBox;

public class SootExpression implements Visitable<SootExpressionVisitor<?>> {

	private final Value sootExpression;

	public SootExpression(final Value sootExpression) {
		this.sootExpression = sootExpression;
	}

	public SootType getType() {
		return new SootType(sootExpression.getType());
	}

	public List<ValueBox> getUseBoxes() {
		return sootExpression.getUseBoxes();
	}

	@Override
	public void apply(final SootExpressionVisitor<?> visitor) {
		sootExpression.apply(visitor);
	}

	@Override
	public boolean equals(final Object expression) {
		return expression instanceof SootExpression
				&& ((SootExpression) expression).sootExpression.equals(sootExpression);
	}

}
