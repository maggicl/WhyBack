package byteback.analysis.transformer;

import byteback.analysis.Vimp;
import byteback.analysis.util.SootBodies;
import byteback.analysis.vimp.AssertionStmt;
import byteback.analysis.vimp.InvariantStmt;
import byteback.analysis.vimp.LogicConstant;
import byteback.util.Lazy;
import java.util.*;
import java.util.function.Supplier;
import soot.Body;
import soot.BodyTransformer;
import soot.Unit;
import soot.Value;
import soot.grimp.GrimpBody;
import soot.jimple.IfStmt;
import soot.jimple.Stmt;
import soot.jimple.toolkits.annotation.logic.Loop;
import soot.jimple.toolkits.annotation.logic.LoopFinder;
import soot.util.Chain;

public class InvariantCheckerTransformer extends BodyTransformer {

	private static final Lazy<InvariantCheckerTransformer> instance = Lazy.from(InvariantCheckerTransformer::new);

	public static InvariantCheckerTransformer v() {
		return instance.get();
	}

	private InvariantCheckerTransformer() {
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
			// if the loop does not have a loop invariant add a dummy loop invariant
			if (loop.getLoopStatements().stream().noneMatch(e -> e instanceof InvariantStmt)) {
				System.out.println("inserting fake invariant for loop " + loop.getLoopStatements());
				// TODO: consider adding detailed warning message
				units.insertBefore(new InvariantStmt(LogicConstant.v(true)), loop.getHead());
			}
		}
	}
}
