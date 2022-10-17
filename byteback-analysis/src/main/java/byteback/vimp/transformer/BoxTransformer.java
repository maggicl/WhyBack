package byteback.vimp.transformer;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import byteback.core.representation.soot.body.SootExpressionVisitor;
import byteback.core.representation.soot.body.SootStatementVisitor;
import soot.Body;
import soot.BodyTransformer;
import soot.Unit;
import soot.UnitBox;
import soot.Value;
import soot.ValueBox;
import soot.grimp.Grimp;
import soot.jimple.AssignStmt;
import soot.jimple.GotoStmt;
import soot.jimple.IfStmt;
import soot.jimple.InvokeExpr;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.TableSwitchStmt;

public class BoxTransformer extends BodyTransformer {

	@Override
	protected void internalTransform(final Body body, String phaseName, Map<String, String> options) {
		internalTransform(body);
	}

	public static void remapBranches(final Body body, Map<Unit, Unit> map) {
		new SootStatementVisitor<>() {

			@Override
			public void caseGotoStmt(final GotoStmt unit) {
				final GotoStmt replaced = (GotoStmt) map.get(unit);
				replaced.setTarget(map.get(replaced.getTarget()));
			}

			@Override
			public void caseIfStmt(final IfStmt unit) {
				final IfStmt replaced = (IfStmt) map.get(unit);
				replaced.setTarget(map.get(replaced.getTarget()));
			}

			@Override
			public void caseLookupSwitchStmt(final LookupSwitchStmt s) {
				final LookupSwitchStmt replaced = (LookupSwitchStmt) map.get(s);
				replaced.setDefaultTarget(map.get(replaced.getDefaultTarget()));
				final Unit[] newTargList = new Unit[replaced.getTargetCount()];

				for (int i = 0; i < newTargList.length; i++) {
					newTargList[i] = map.get(replaced.getTarget(i));
				}

				replaced.setTargets(newTargList);
			}

			@Override
			public void caseTableSwitchStmt(TableSwitchStmt s) {
				final TableSwitchStmt replaced = (TableSwitchStmt) map.get(s);
				replaced.setDefaultTarget(map.get(replaced.getDefaultTarget()));
				final int tc = replaced.getHighIndex() - replaced.getLowIndex() + 1;
				final LinkedList<Unit> newTargList = new LinkedList<Unit>();

				for (int i = 0; i < tc; i++) {
					newTargList.add(map.get(replaced.getTarget(i)));
				}

				replaced.setTargets(newTargList);
			}
		};
	}

	public void internalTransform(final Body body) {
		final var oldToNew = new HashMap<Unit, Unit>(body.getUnits().size() * 2 + 1, 0.7f);

		for (final UnitBox ubox : body.getAllUnitBoxes()) {
			final Unit unit = ubox.getUnit();

			unit.apply(new SootStatementVisitor<>() {

				private void substitute(final Unit source) {
					oldToNew.put(unit, source);
					ubox.setUnit(source);
				}

				@Override
				public void caseAssignStmt(final AssignStmt unit) {
					substitute(Grimp.v().newAssignStmt(unit));
				}

			});

			for (final ValueBox vbox : unit.getUseBoxes()) {
				final Value value = vbox.getValue();

				value.apply(new SootExpressionVisitor<>() {

					@Override
					public void caseInvokeExpr(final InvokeExpr value) {
						vbox.setValue(Grimp.v().newExpr(value));
					}

				});
			}
		}

		remapBranches(body, oldToNew);
	}

}
