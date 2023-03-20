package byteback.analysis.transformer;

import byteback.analysis.Namespace;
import byteback.analysis.Vimp;
import byteback.analysis.vimp.VoidConstant;
import byteback.util.Lazy;
import byteback.util.ListHashMap;
import byteback.util.Stacks;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import soot.Body;
import soot.BodyTransformer;
import soot.RefType;
import soot.Scene;
import soot.Trap;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.grimp.Grimp;
import soot.grimp.GrimpBody;
import soot.jimple.AssignStmt;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.GotoStmt;
import soot.jimple.IfStmt;
import soot.jimple.InstanceOfExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.NeExpr;
import soot.jimple.ThrowStmt;
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
		final Chain<Trap> traps = body.getTraps();
		final ListHashMap<Unit, Trap> startToTraps = new ListHashMap<>();
		final ListHashMap<Unit, Trap> endToTraps = new ListHashMap<>();
		final HashSet<Unit> trapHandlers = new HashSet<>();
		final Stack<Trap> activeTraps = new Stack<>();
		final Unit terminalUnit = Grimp.v().newReturnVoidStmt();
		units.addLast(terminalUnit);
		final Iterator<Unit> unitIterator = units.snapshotIterator();
		units.addFirst(Grimp.v().newAssignStmt(Vimp.v().newCaughtExceptionRef(), VoidConstant.v()));

		for (final Trap trap : traps) {
			startToTraps.add(trap.getBeginUnit(), trap);
			endToTraps.add(trap.getEndUnit(), trap);
			trapHandlers.add(trap.getHandlerUnit());
		}

		for (final Unit handler : trapHandlers) {
			final AssignStmt assignment = Grimp.v().newAssignStmt(Vimp.v().newCaughtExceptionRef(), VoidConstant.v());
			units.insertAfter(assignment, handler);
		}

		while (unitIterator.hasNext()) {
			final Unit unit = unitIterator.next();
			final List<Trap> startedTraps = startToTraps.get(unit);
			final List<Trap> endedTraps = endToTraps.get(unit);

			if (startedTraps != null) {
				Stacks.pushAll(activeTraps, startToTraps.get(unit));
			}

			if (endedTraps != null) {
				Stacks.popAll(activeTraps, endToTraps.get(unit));
			}

			if (unit instanceof ThrowStmt throwUnit) {
				if (throwUnit.getOp().getType() instanceof RefType throwType) {
					for (final Trap activeTrap : activeTraps) {
						final RefType trapType = activeTrap.getException().getType();

						if (Scene.v().getFastHierarchy().isSubclass(throwType.getSootClass(),
								trapType.getSootClass())) {
							final GotoStmt guardUnit = Grimp.v().newGotoStmt(activeTrap.getHandlerUnit());
							units.insertAfter(guardUnit, unit);
							units.remove(unit);
							break;
						}
					}
				}
			} else {
				for (final ValueBox vbox : unit.getUseBoxes()) {
					final Value value = vbox.getValue();

					if (value instanceof InvokeExpr invoke && !Namespace.isPureMethod(invoke.getMethod())) {
						Unit currentGuardUnit = unit;

						for (final Trap trap : activeTraps) {
							final CaughtExceptionRef eref = Vimp.v().newCaughtExceptionRef();
							final InstanceOfExpr condition = Vimp.v().newInstanceOfExpr(eref,
									trap.getException().getType());
							final Unit newGuardUnit = Vimp.v().newIfStmt(condition, trap.getHandlerUnit());
							units.insertAfter(newGuardUnit, currentGuardUnit);
							currentGuardUnit = newGuardUnit;
						}

						final CaughtExceptionRef eref = Vimp.v().newCaughtExceptionRef();
						final NeExpr condition = Vimp.v().newNeExpr(eref, VoidConstant.v());
						final IfStmt guardUnit = Vimp.v().newIfStmt(condition, terminalUnit);
						units.insertAfter(guardUnit, currentGuardUnit);
					}
				}
			}
		}
	}

}
