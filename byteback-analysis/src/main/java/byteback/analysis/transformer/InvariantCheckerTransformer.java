package byteback.analysis.transformer;

import byteback.analysis.Vimp;
import byteback.analysis.tags.PositionTag;
import byteback.analysis.util.SootBodies;
import byteback.analysis.vimp.InvariantStmt;
import byteback.analysis.vimp.LogicConstant;
import byteback.analysis.vimp.VoidConstant;
import byteback.util.Lazy;
import java.util.Collection;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.Unit;
import soot.grimp.GrimpBody;
import soot.jimple.AssignStmt;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.Stmt;
import soot.jimple.ThrowStmt;
import soot.jimple.toolkits.annotation.logic.Loop;
import soot.util.Chain;

public class InvariantCheckerTransformer extends BodyTransformer {

	private static final Lazy<InvariantCheckerTransformer> instance = Lazy.from(InvariantCheckerTransformer::new);

	private InvariantCheckerTransformer() {
	}

	public static InvariantCheckerTransformer v() {
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

	public void transformBody(final Body body) {
		final Chain<Unit> units = body.getUnits();
		final Collection<Loop> loops = SootBodies.getLoops(body);

		for (final Loop loop : loops) {
			// do not falsely detect try-finally blocks as non-exception-throwing loops:
			// https://github.com/soot-oss/soot/issues/982
			final boolean nothingThrown = loop.getLoopStatements().stream().noneMatch(e ->
					e instanceof ThrowStmt
							|| (e instanceof AssignStmt a
									&& a.getLeftOp() instanceof CaughtExceptionRef
									&& a.getRightOp() != VoidConstant.v()));

			// if the loop does not have a loop invariant add a dummy loop invariant
			if (loop.getLoopStatements().stream().noneMatch(e -> e instanceof InvariantStmt)) {
				final Stmt stmt = loop.getHead();
				if (stmt.hasTag("PositionTag")) {
					final PositionTag tag = (PositionTag) stmt.getTag("PositionTag");
					System.out.printf("Missing invariant: %s, line %d%n", tag.file, tag.lineNumber);
				} else {
					System.out.println("Missing invariant: " + loop.getLoopStatements());
				}

				units.insertBefore(
						new InvariantStmt(nothingThrown
								? Vimp.v().newEqExpr(Vimp.v().newCaughtExceptionRef(), VoidConstant.v())
								: LogicConstant.v(true), true),
						loop.getHead()
				);
			}
		}
	}
}
