package byteback.vimp.transformer;

import java.util.Iterator;
import java.util.Map;

import byteback.core.representation.soot.body.SootExpressionVisitor;
import byteback.core.representation.soot.body.SootStatementVisitor;
import byteback.vimp.Vimp;
import byteback.vimp.internal.LogicConstant;
import soot.Body;
import soot.BodyTransformer;
import soot.BooleanType;
import soot.PatchingChain;
import soot.Type;
import soot.Unit;
import soot.ValueBox;
import soot.jimple.AndExpr;
import soot.jimple.AssignStmt;
import soot.jimple.IntConstant;
import soot.jimple.JimpleBody;
import soot.jimple.OrExpr;

public class LogicValueTransformer extends BodyTransformer {

	@Override
	protected void internalTransform(final Body body, String phaseName, Map<String, String> options) {
		if (body instanceof JimpleBody jimpleBody) {
			internalTransform(jimpleBody);
		} else {
			throw new IllegalArgumentException("Can transform only Jimple");
		}
	}

	private static void substituteBooleanExpr(final ValueBox vbox) {
		vbox.getValue().apply(new SootExpressionVisitor<>() {

			@Override
			public void caseIntConstant(final IntConstant constant) {
				vbox.setValue(LogicConstant.v(constant.value > 0));
			}

			@Override
			public void caseAndExpr(final AndExpr value) {
				assert value.getOp1().getType() == BooleanType.v();
				assert value.getOp2().getType() == BooleanType.v();

				vbox.setValue(Vimp.v().newLogicAndExpr(value.getOp1(), value.getOp2()));
			}

			@Override
			public void caseOrExpr(final OrExpr value) {
				assert value.getOp1().getType() == BooleanType.v();
				assert value.getOp2().getType() == BooleanType.v();

				vbox.setValue(Vimp.v().newLogicOrExpr(value.getOp1(), value.getOp2()));
			}

		});
	}

	protected void internalTransform(final JimpleBody body) {
		final PatchingChain<Unit> units = body.getUnits();
		final Iterator<Unit> iterator = units.snapshotIterator();

		while (iterator.hasNext()) {
			final Unit unit = iterator.next();

			unit.apply(new SootStatementVisitor<>() {

				@Override
				public void caseAssignStmt(final AssignStmt unit) {
					final Type type = unit.getLeftOp().getType();

					if (type == BooleanType.v()) {
						final ValueBox vbox = unit.getRightOpBox();
						substituteBooleanExpr(vbox);
					}
				}

			});
		}
	}

}
