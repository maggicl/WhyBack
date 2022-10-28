package byteback.analysis.transformer;

import byteback.analysis.JimpleStmtSwitch;
import byteback.util.Lazy;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.Unit;
import soot.UnitBox;
import soot.grimp.Grimp;
import soot.jimple.AssignStmt;
import soot.jimple.IfStmt;

public class AggregationTransformer extends BodyTransformer implements UnitTransformer {

	private static final Lazy<AggregationTransformer> instance = Lazy.from(AggregationTransformer::new);

	public static AggregationTransformer v() {
		return instance.get();
	}

	@Override
	protected void internalTransform(final Body body, final String phaseName, final Map<String, String> options) {
		transformBody(body);
	}

	@Override
	public void transformUnit(final UnitBox unitBox) {
		final Unit unit = unitBox.getUnit();

		unit.apply(new JimpleStmtSwitch<>() {

			@Override
			public void caseAssignStmt(final AssignStmt unit) {
				unitBox.setUnit(Grimp.v().newAssignStmt(unit));
			}

			@Override
			public void caseIfStmt(final IfStmt unit) {
				unitBox.setUnit(Grimp.v().newIfStmt(unit));
			}

		});
	}

}
