package byteback.whyml.vimp.expr;

import byteback.analysis.JimpleStmtSwitch;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.expr.UnitLiteral;
import byteback.whyml.syntax.function.CFGLabel;
import byteback.whyml.syntax.statement.CFGTerminator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import soot.Unit;
import soot.Value;
import soot.jimple.GotoStmt;
import soot.jimple.IfStmt;
import soot.jimple.IntConstant;
import soot.jimple.LookupSwitchStmt;
import soot.jimple.ReturnStmt;
import soot.jimple.ReturnVoidStmt;
import soot.jimple.TableSwitchStmt;

public class CFGTerminatorExtractor extends JimpleStmtSwitch<Optional<CFGTerminator>> {
	private final ProcedureExpressionExtractor expressionExtractor;
	private final Map<Unit, CFGLabel> labelMap;
	private final Optional<Unit> fallThrough;

	public CFGTerminatorExtractor(ProcedureExpressionExtractor expressionExtractor,
								  Optional<Unit> fallThrough,
								  Map<Unit, CFGLabel> labelMap) {
		this.expressionExtractor = expressionExtractor;
		this.labelMap = labelMap;
		this.fallThrough = fallThrough;
	}

	public CFGTerminator visitOrFallThrough(Unit unit) {
		return visit(unit).orElseGet(() ->
				new CFGTerminator.Goto(
						toLabel(fallThrough.orElseThrow(() ->
								new IllegalStateException("no BB terminator instruction or fall through")))));
	}

	private CFGLabel toLabel(Unit unit) {
		return Objects.requireNonNull(labelMap.get(unit));
	}

	@Override
	public void caseReturnVoidStmt(final ReturnVoidStmt returnStatement) {
		setResult(Optional.of(new CFGTerminator.Return(UnitLiteral.INSTANCE)));
	}

	@Override
	public void caseReturnStmt(final ReturnStmt returnStatement) {
		setResult(Optional.of(new CFGTerminator.Return(expressionExtractor.visit(returnStatement.getOp()))));
	}

	@Override
	public void caseLookupSwitchStmt(final LookupSwitchStmt switchStatement) {
		final Iterator<Unit> targets = switchStatement.getTargets().iterator();
		final Iterator<IntConstant> values = switchStatement.getLookupValues().iterator();
		final Expression key = expressionExtractor.visit(switchStatement.getKey());
		final Map<Integer, CFGLabel> targetMap = new HashMap<>();

		while (targets.hasNext() && values.hasNext()) {
			final Unit target = targets.next();
			final CFGLabel label = toLabel(target);

			final IntConstant value = values.next();
			targetMap.put(value.value, label);
		}

		final CFGLabel defaultTarget = toLabel(switchStatement.getDefaultTarget());
		setResult(Optional.of(new CFGTerminator.Switch(key, targetMap, defaultTarget)));
	}

	@Override
	public void caseTableSwitchStmt(final TableSwitchStmt switchStatement) {
		final Expression key = expressionExtractor.visit(switchStatement.getKey());
		final Map<Integer, CFGLabel> targetMap = new HashMap<>();

		for (int i = 0; i <= switchStatement.getHighIndex() - switchStatement.getLowIndex(); ++i) {
			final Unit target = switchStatement.getTarget(i);
			final CFGLabel label = Objects.requireNonNull(labelMap.get(target));

			final int offsetIndex = switchStatement.getLowIndex() + i;
			targetMap.put(offsetIndex, label);
		}

		final CFGLabel defaultTarget = toLabel(switchStatement.getDefaultTarget());
		setResult(Optional.of(new CFGTerminator.Switch(key, targetMap, defaultTarget)));
	}

	@Override
	public void caseGotoStmt(final GotoStmt gotoStatement) {
		final Unit targetUnit = gotoStatement.getTarget();
		final CFGLabel label = labelMap.get(targetUnit);

		setResult(Optional.of(new CFGTerminator.Goto(label)));
	}

	@Override
	public void caseIfStmt(final IfStmt ifStatement) {
		final Value condition = ifStatement.getCondition();
		final Expression key = expressionExtractor.visit(condition);

		final Unit thenBranch = ifStatement.getTarget();
		final CFGLabel thenLabel = toLabel(thenBranch);

		final Unit elseBranch = fallThrough.orElseThrow(() ->
				new IllegalStateException("fall through must be possible when an if statement terminates a basic block"));
		final CFGLabel elseLabel = toLabel(elseBranch);

		setResult(Optional.of(new CFGTerminator.If(key, thenLabel, elseLabel)));
	}

	@Override
	public void caseDefault(Unit o) {
		throw new IllegalStateException("unit is not a valid terminator for a basic block: " + o);
	}
}
