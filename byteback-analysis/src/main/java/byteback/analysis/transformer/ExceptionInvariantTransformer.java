package byteback.analysis.transformer;

import byteback.analysis.Vimp;
import byteback.analysis.vimp.VoidConstant;
import byteback.util.Lazy;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.Body;
import soot.BodyTransformer;
import soot.Unit;
import soot.Value;
import soot.grimp.GrimpBody;
import soot.jimple.Stmt;
import soot.jimple.toolkits.annotation.logic.Loop;
import soot.jimple.toolkits.annotation.logic.LoopFinder;
import soot.util.Chain;

public class ExceptionInvariantTransformer extends BodyTransformer {

	private static final Lazy<ExceptionInvariantTransformer> instance = Lazy.from(ExceptionInvariantTransformer::new);

	public static ExceptionInvariantTransformer v() {
		return instance.get();
	}

	private ExceptionInvariantTransformer() {
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
		final LoopFinder loopFinder = new LoopFinder();
		final Set<Loop> loops = loopFinder.getLoops(body);

		for (final Loop loop : loops) {
			final List<Stmt> loopUnits = loop.getLoopStatements();

			if (loopUnits.size() > 1) {
				final Unit loopStart = loopUnits.get(0);
				final Value invariantValue = Vimp.v().newEqExpr(Vimp.v().newCaughtExceptionRef(), VoidConstant.v());
				final Unit invariantUnit = Vimp.v().newInvariantStmt(invariantValue);
				units.insertAfter(invariantUnit, loopStart);
			}
		}
	}

}
