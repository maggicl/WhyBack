package byteback.analysis.transformer;

import java.util.List;
import java.util.Map;
import java.util.Set;

import byteback.analysis.Vimp;
import byteback.analysis.vimp.InvariantStmt;
import byteback.util.Lazy;
import soot.Body;
import soot.BodyTransformer;
import soot.Unit;
import soot.Value;
import soot.grimp.GrimpBody;
import soot.jimple.Stmt;
import soot.jimple.toolkits.annotation.logic.Loop;
import soot.jimple.toolkits.annotation.logic.LoopFinder;
import soot.util.Chain;

public class InvariantExpander extends BodyTransformer {

	private static final Lazy<InvariantExpander> instance = Lazy.from(InvariantExpander::new);

	public static InvariantExpander v() {
		return instance.get();
	}

	private InvariantExpander() {
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
		final LoopFinder loopFinder = new LoopFinder();
		final Set<Loop> loops = loopFinder.getLoops(body);
		final Chain<Unit> units = body.getUnits();

		for (Loop loop : loops) {
			final List<Stmt> statements = loop.getLoopStatements();

			for (final Stmt stmt : statements) {
				if (stmt instanceof InvariantStmt invariantStmt) {
					final Value condition = invariantStmt.getCondition();
					units.insertBefore(Vimp.v().newAssertionStmt(condition), loop.getHead());
					units.insertAfter(Vimp.v().newAssumptionStmt(condition), loop.getHead());
					units.insertBefore(Vimp.v().newAssertionStmt(condition), loop.getBackJumpStmt());
					units.insertAfter(Vimp.v().newAssumptionStmt(condition), loop.getBackJumpStmt());
				}
			}
		}
	}

}
