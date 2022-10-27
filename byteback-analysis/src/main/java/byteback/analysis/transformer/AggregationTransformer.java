package byteback.analysis.transformer;

import static byteback.analysis.transformer.UnitTransformer.putStatement;

import byteback.analysis.JimpleStmtSwitch;
import byteback.analysis.JimpleValueSwitch;
import byteback.util.Lazy;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.Unit;
import soot.UnitBox;
import soot.Value;
import soot.ValueBox;
import soot.grimp.Grimp;
import soot.jimple.AssignStmt;
import soot.jimple.InvokeExpr;

public class AggregationTransformer extends BodyTransformer implements UnitValueTransformer {

	private static final Lazy<AggregationTransformer> instance = Lazy.from(AggregationTransformer::new);

	public static AggregationTransformer v() {
		return instance.get();
	}

	@Override
	protected void internalTransform(final Body body, String phaseName, Map<String, String> options) {
		transformBody(body);
	}

	@Override
	public void transformUnit(final UnitBox unitBox) {
		final Unit unit = unitBox.getUnit();

		unit.apply(new JimpleStmtSwitch<>() {

			@Override
			public void caseAssignStmt(final AssignStmt unit) {
				putStatement(unitBox, Grimp.v().newAssignStmt(unit));
			}

		});
	}

	@Override
	public void transformValue(final ValueBox valueBox) {
		final Value value = valueBox.getValue();

		value.apply(new JimpleValueSwitch<>() {

			@Override
			public void caseInvokeExpr(final InvokeExpr value) {
				valueBox.setValue(Grimp.v().newExpr(value));
			}

		});
	}

}
