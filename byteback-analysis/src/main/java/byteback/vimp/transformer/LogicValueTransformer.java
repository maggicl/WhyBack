package byteback.vimp.transformer;

import java.util.Map;

import byteback.core.representation.soot.body.SootExpressionVisitor;
import byteback.core.representation.soot.body.SootStatementVisitor;
import byteback.core.util.Lazy;
import byteback.vimp.Vimp;
import byteback.vimp.internal.LogicConstant;
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

public class LogicValueTransformer extends BodyTransformer {
		
	private static final Lazy<LogicValueTransformer> instance = Lazy.from(() -> new LogicValueTransformer());

	public static LogicValueTransformer v() {
		return instance.get();
	}

	private LogicValueTransformer() {
	}

	@Override
	protected void internalTransform(final Body body, String phaseName, Map<String, String> options) {
		if (body instanceof JimpleBody jimpleBody) {
			internalTransform(jimpleBody);
		} else {
			throw new IllegalArgumentException("Can only transform Jimple");
		}
	}

	public void transformValue(final ValueBox vbox) {
		vbox.getValue().apply(new SootExpressionVisitor<>() {

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
		unit.apply(new SootStatementVisitor<>() {

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

	public void internalTransform(final JimpleBody body) {
		for (final UnitBox ubox : body.getAllUnitBoxes()) {
			transformUnit(ubox);
		}
	}

}
