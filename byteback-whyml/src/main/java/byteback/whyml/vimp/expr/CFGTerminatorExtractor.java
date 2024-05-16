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
import soot.jimple.ThrowStmt;

public class CFGTerminatorExtractor extends JimpleStmtSwitch<Optional<CFGTerminator>> {
	private final ProgramExpressionExtractor expressionExtractor;
	private final Map<Unit, CFGLabel> labelMap;
	private final Optional<Unit> fallThrough;
	private Optional<CFGTerminator> result;

	public CFGTerminatorExtractor(ProgramExpressionExtractor expressionExtractor,
								  Optional<Unit> fallThrough,
								  Map<Unit, CFGLabel> labelMap) {
		this.expressionExtractor = expressionExtractor;
		this.labelMap = labelMap;
		this.fallThrough = fallThrough;
	}

	@Override
	public void setResult(Optional<CFGTerminator> result) {
		this.result = result;
	}

	@Override
	public Optional<CFGTerminator> getResult() {
		return this.result;
	}

	public CFGTerminator visitOrFallThrough(Unit unit) {
		visit(unit);
		return result.orElseGet(() ->
				new CFGTerminator.Goto(
						toLabel(fallThrough.orElseThrow(() ->
								new WhyTranslationException(unit, "non-terminating instruction '%s' cannot fall through"
										.formatted(unit.getClass().getName()))))));
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
	public void caseThrowStmt(ThrowStmt stmt) {
		setResult(Optional.of(new CFGTerminator.Throw(expressionExtractor.visit(stmt.getOp()))));
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
				new WhyTranslationException(ifStatement, "IfStmt cannot fall through"));
		final CFGLabel elseLabel = toLabel(elseBranch);

		setResult(Optional.of(new CFGTerminator.If(key, thenLabel, elseLabel)));
	}

	@Override
	public void caseDefault(Unit o) {
		setResult(Optional.empty());
	}
}
