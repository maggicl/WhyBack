package byteback.analysis.transformer;

import byteback.analysis.Vimp;
import byteback.util.Lazy;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.Type;
import soot.Unit;
import soot.Value;
import soot.grimp.Grimp;
import soot.grimp.GrimpBody;
import soot.jimple.AssignStmt;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;
import soot.jimple.NumericConstant;
import soot.jimple.Stmt;
import soot.jimple.internal.AbstractSwitchStmt;
import soot.jimple.internal.JLookupSwitchStmt;
import soot.jimple.internal.JTableSwitchStmt;
import soot.util.Chain;

public class SwitchToIfTransformer extends BodyTransformer {
	private static final Lazy<SwitchToIfTransformer> instance = Lazy.from(SwitchToIfTransformer::new);

	public static SwitchToIfTransformer v() {
		return instance.get();
	}

	@Override
	public void internalTransform(final Body body, final String phaseName, final Map<String, String> options) {
		if (body instanceof GrimpBody) {
			transformBody(body);
		} else {
			throw new IllegalArgumentException("Can only transform Grimp");
		}
	}

	public Local addAndGetLocal(final Body body, final Type type, AtomicInteger switchCounter) {
		final Local switchLocal = Grimp.v().newLocal("$switch%d".formatted(switchCounter.incrementAndGet()), type);
		body.getLocals().addLast(switchLocal);
		return switchLocal;
	}

	public void transformSwitchStmt(Body body,
									AbstractSwitchStmt switchStmt,
									List<IntConstant> values,
									AtomicInteger switchCounter) {
		final Chain<Unit> units = body.getUnits();
		final Value switchKey = switchStmt.getKey();
		final Local switchLocal = addAndGetLocal(body, switchKey.getType(), switchCounter);

		final AssignStmt setSwitchLocal = Grimp.v().newAssignStmt(switchLocal, switchKey);
		units.insertBefore(setSwitchLocal, switchStmt);
		switchStmt.redirectJumpsToThisTo(setSwitchLocal);
		units.remove(switchStmt);

		Stmt lastStatement = setSwitchLocal;

		for (int i = 0; i < values.size(); i++) {
			final NumericConstant value = values.get(i);
			final Unit target = switchStmt.getTarget(i);

			final Stmt ifStmt = Vimp.v().newIfStmt(Vimp.v().newEqExpr(switchLocal, value), target);
			units.insertAfter(ifStmt, lastStatement);
			lastStatement = ifStmt;
		}

		final Stmt defaultStmt = Jimple.v().newGotoStmt(switchStmt.getDefaultTarget());
		units.insertAfter(defaultStmt, lastStatement);
	}

	public void transformBody(final Body body) {
		final Chain<Unit> units = body.getUnits();
		final Iterator<Unit> unitIterator = units.snapshotIterator();
		final AtomicInteger switchCounter = new AtomicInteger();

		while (unitIterator.hasNext()) {
			final Unit unit = unitIterator.next();

			if (unit instanceof JTableSwitchStmt tableSwitchStmt) {
				transformSwitchStmt(
						body,
						tableSwitchStmt,
						IntStream.rangeClosed(tableSwitchStmt.getLowIndex(), tableSwitchStmt.getHighIndex())
								.mapToObj(IntConstant::v)
								.toList(),
						switchCounter
				);
			} else if (unit instanceof JLookupSwitchStmt lookupSwitchStmt) {
				transformSwitchStmt(
						body,
						lookupSwitchStmt,
						lookupSwitchStmt.getLookupValues(),
						switchCounter
				);
			}
		}
	}
}

