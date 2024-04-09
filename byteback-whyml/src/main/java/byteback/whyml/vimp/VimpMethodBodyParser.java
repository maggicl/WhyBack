package byteback.whyml.vimp;

import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.vimp.expr.FunctionBodyExtractor;
import soot.SootMethod;

public class VimpMethodBodyParser {
	private final FunctionBodyExtractor functionBodyExtractor;

	public VimpMethodBodyParser(FunctionBodyExtractor functionBodyExtractor) {
		this.functionBodyExtractor = functionBodyExtractor;
	}

	public Expression parse(SootMethod method) {
		return functionBodyExtractor.visit(method.retrieveActiveBody());
	}
}
