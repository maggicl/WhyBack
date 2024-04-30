package byteback.whyml.vimp;

import byteback.analysis.util.SootBodies;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.vimp.expr.FunctionBodyExtractor;
import java.util.Optional;
import soot.SootMethod;
import soot.toolkits.graph.BlockGraph;

public class VimpMethodBodyParser {
	private final FunctionBodyExtractor functionBodyExtractor;

	public VimpMethodBodyParser(FunctionBodyExtractor functionBodyExtractor) {
		this.functionBodyExtractor = functionBodyExtractor;
	}

	public Expression parseSpec(SootMethod method) {
		if (!method.hasActiveBody()) {
			throw new IllegalStateException("Spec method " + method.getSignature() + " has no active body");
		}

		return functionBodyExtractor.visit(method.retrieveActiveBody());
	}

	public Optional<Expression> parseProgram(SootMethod method) {
		if (method.hasActiveBody()) {
			final BlockGraph bg = SootBodies.getBlockGraph(method.getActiveBody());
			//System.out.println("bg " + bg);

			return Optional.empty(); // todo change
		} else {
			return Optional.empty();
		}
	}
}
