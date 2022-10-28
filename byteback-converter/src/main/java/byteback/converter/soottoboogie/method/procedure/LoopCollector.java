package byteback.converter.soottoboogie.method.procedure;

import byteback.analysis.JimpleStmtSwitch;
import byteback.analysis.util.SootBodies;
import byteback.converter.soottoboogie.method.StatementConversionException;
import byteback.frontend.boogie.ast.AssertStatement;
import byteback.frontend.boogie.ast.AssumeStatement;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.ExtensionPoint;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import soot.Body;
import soot.Unit;
import soot.jimple.IfStmt;
import soot.jimple.toolkits.annotation.logic.Loop;

public class LoopCollector {

	public static class LoopContext {

		private final Loop loop;

		private final ExtensionPoint assertionPoint;

		private final ExtensionPoint assumptionPoint;

		public LoopContext(final Loop loop) {
			this.loop = loop;
			this.assertionPoint = new ExtensionPoint();
			this.assumptionPoint = new ExtensionPoint();
		}

		public Loop getLoop() {
			return loop;
		}

		public void addInvariant(final Expression invariant) {
			assertionPoint.addStatement(new AssertStatement(invariant));
			assumptionPoint.addStatement(new AssumeStatement(invariant));
		}

		public ExtensionPoint getAssertionPoint() {
			return assertionPoint;
		}

		public ExtensionPoint getAssumptionPoint() {
			return assumptionPoint;
		}

	}

	private final Map<Unit, LoopContext> headIndex;

	private final Map<Unit, LoopContext> backJumpIndex;

	private final Map<Unit, LoopContext> exitIndex;

	private final Map<Unit, LoopContext> exitTargetIndex;

	public LoopCollector() {
		this.headIndex = new HashMap<>();
		this.backJumpIndex = new HashMap<>();
		this.exitIndex = new HashMap<>();
		this.exitTargetIndex = new HashMap<>();
	}

	public void collect(final Body body) {
		for (Loop loop : SootBodies.getLoops(body)) {
			final LoopContext loopContext = new LoopContext(loop);
			headIndex.put(loop.getHead(), loopContext);
			backJumpIndex.put(loop.getBackJumpStmt(), loopContext);

			for (Unit exit : loop.getLoopExits()) {
				exit.apply(new JimpleStmtSwitch<>() {

					@Override
					public void caseIfStmt(final IfStmt ifStatement) {
						exitIndex.put(ifStatement, loopContext);
						exitTargetIndex.put(ifStatement.getTarget(), loopContext);
					}

					@Override
					public void caseDefault(final Unit unit) {
						throw new StatementConversionException(unit, "Cannot identify exit target from " + unit);
					}

				});
			}
		}
	}

	public Optional<LoopContext> getByHead(final Unit unit) {
		return Optional.ofNullable(headIndex.get(unit));
	}

	public Optional<LoopContext> getByBackJump(final Unit unit) {
		return Optional.ofNullable(backJumpIndex.get(unit));
	}

	public Optional<LoopContext> getByExit(final Unit unit) {
		return Optional.ofNullable(exitIndex.get(unit));
	}

	public Optional<LoopContext> getByExitTarget(final Unit unit) {
		return Optional.ofNullable(exitTargetIndex.get(unit));
	}

	public Collection<LoopContext> getLoopContexts() {
		return headIndex.values();
	}

}
