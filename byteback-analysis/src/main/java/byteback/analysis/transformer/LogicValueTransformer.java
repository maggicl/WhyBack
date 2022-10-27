package byteback.analysis.transformer;

import byteback.analysis.JimpleStmtSwitch;
import byteback.analysis.JimpleValueSwitch;
import byteback.analysis.Vimp;
import byteback.analysis.vimp.LogicConstant;
import byteback.util.Lazy;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.BooleanType;
import soot.Type;
import soot.Unit;
import soot.UnitBox;
import soot.ValueBox;
import soot.jimple.AndExpr;
import soot.jimple.AssignStmt;
import soot.jimple.IntConstant;
import soot.jimple.JimpleBody;
import soot.jimple.NegExpr;
import soot.jimple.OrExpr;

public class LogicValueTransformer extends BodyTransformer implements ValueTransformer {

	private static final Lazy<LogicValueTransformer> instance = Lazy.from(LogicValueTransformer::new);

	public static LogicValueTransformer v() {
		return instance.get();
	}

	private LogicValueTransformer() {
	}

	@Override
	protected void internalTransform(final Body body, String phaseName, Map<String, String> options) {
		if (body instanceof JimpleBody jimpleBody) {
			transformBody(jimpleBody);
		} else {
			throw new IllegalArgumentException("Can only transform Jimple");
		}
	}

	public void transformValue(final ValueBox vbox) {
		vbox.getValue().apply(new JimpleValueSwitch<>() {

			@Override
			public void caseIntConstant(final IntConstant constant) {
				vbox.setValue(LogicConstant.v(constant.value > 0));
			}

			@Override
			public void caseAndExpr(final AndExpr value) {
				vbox.setValue(Vimp.v().newLogicAndExpr(value.getOp1Box(), value.getOp2Box()));
			}

			@Override
			public void caseOrExpr(final OrExpr value) {
				vbox.setValue(Vimp.v().newLogicOrExpr(value.getOp1Box(), value.getOp2Box()));
			}

			@Override
			public void caseNegExpr(final NegExpr value) {
				vbox.setValue(Vimp.v().newLogicNotExpr(value.getOpBox()));
			}

		});
	}

	public void transformUnit(final Unit unit) {
		unit.apply(new JimpleStmtSwitch<>() {

			@Override
			public void caseAssignStmt(final AssignStmt unit) {
				final Type type = unit.getLeftOp().getType();

				if (type == BooleanType.v()) {
					final ValueBox vbox = unit.getRightOpBox();
					transformValue(vbox);
				}
			}

		});
	}

	public void transformUnit(final UnitBox ubox) {
		transformUnit(ubox.getUnit());
	}

}
