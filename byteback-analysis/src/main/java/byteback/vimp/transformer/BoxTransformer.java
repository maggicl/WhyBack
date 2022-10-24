package byteback.vimp.transformer;

import java.util.Map;

import static byteback.vimp.transformer.UnitTransformer.putStatement;

import byteback.core.representation.soot.body.SootExpressionVisitor;
import byteback.core.representation.soot.body.SootStatementVisitor;
import byteback.core.util.Lazy;
import soot.Body;
import soot.BodyTransformer;
import soot.Unit;
import soot.UnitBox;
import soot.Value;
import soot.ValueBox;
import soot.grimp.Grimp;
import soot.jimple.AssignStmt;
import soot.jimple.InvokeExpr;

public class BoxTransformer extends BodyTransformer implements UnitValueTransformer {

	private static final Lazy<BoxTransformer> instance = Lazy.from(() -> new BoxTransformer());

	public static BoxTransformer v() {
		return instance.get();
	}

	@Override
	protected void internalTransform(final Body body, String phaseName, Map<String, String> options) {
		internalTransform(body);
	}

	@Override
	public void transformUnit(final UnitBox ubox) {
		final Unit unit = ubox.getUnit();

		unit.apply(new SootStatementVisitor<>() {

			@Override
			public void caseAssignStmt(final AssignStmt unit) {
				putStatement(ubox, Grimp.v().newAssignStmt(unit));
			}

		});
	}

	@Override
	public void transformValue(final ValueBox vbox) {
		final Value value = vbox.getValue();

		value.apply(new SootExpressionVisitor<>() {

			@Override
			public void caseInvokeExpr(final InvokeExpr value) {
				vbox.setValue(Grimp.v().newExpr(value));
			}

		});
	}

}
