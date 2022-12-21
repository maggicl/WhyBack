package byteback.analysis.transformer;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;

import byteback.analysis.Namespace;
import byteback.analysis.Vimp;
import byteback.util.Lazy;
import soot.Body;
import soot.BodyTransformer;
import soot.Trap;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.InstanceOfExpr;
import soot.grimp.GrimpBody;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.IfStmt;
import soot.jimple.InvokeExpr;
import soot.util.Chain;

public class GuardTransformer extends BodyTransformer {

	private static final Lazy<GuardTransformer> instance = Lazy.from(GuardTransformer::new);

	public static GuardTransformer v() {
		return instance.get();
	}

	private GuardTransformer() {
	}

	@Override
	public void internalTransform(final Body body, final String phaseName, final Map<String, String> options) {
		if (body instanceof GrimpBody) {
			transformBody(body);
		} else {
			throw new IllegalArgumentException("Can only transform Grimp");
		}
	}

	public void transformBody(final Body body) {
		final Chain<Unit> units = body.getUnits();
		final Iterator<Unit> unitIterator = units.snapshotIterator();
		final Chain<Trap> traps = body.getTraps();
		final HashMap<Unit, Trap> startToTrap = new HashMap<>();
		final HashMap<Unit, Trap> endToTrap = new HashMap<>();
		final Stack<Trap> activeTraps = new Stack<>();

		for (final Trap trap : traps) {
			startToTrap.put(trap.getBeginUnit(), trap);
			endToTrap.put(trap.getEndUnit(), trap);
		}

		while (unitIterator.hasNext()) {
			final Unit unit = unitIterator.next();
			final Trap startedTrap = startToTrap.get(unit);
			final Trap endedTrap = endToTrap.get(unit);

			if (startedTrap != null) {
				activeTraps.push(startToTrap.get(unit));
			}

			if (endedTrap != null) {
				assert activeTraps.peek() == endedTrap;
				activeTraps.pop();
			}

			for (final ValueBox vbox : unit.getUseBoxes()) {
				final Value value = vbox.getValue();

				for (final Trap trap : activeTraps) {
					if (value instanceof InvokeExpr invoke
							&& !Namespace.isPureMethod(invoke.getMethod())) {
						final CaughtExceptionRef eref = Vimp.v().newCaughtExceptionRef();
						final InstanceOfExpr condition = Vimp.v().newInstanceOfExpr(eref, trap.getException().getType());
						final IfStmt guardUnit = Vimp.v().newIfStmt(condition, trap.getHandlerUnit());
						units.insertAfter(guardUnit, unit);
					}
				}
			}
		}
	}

}
