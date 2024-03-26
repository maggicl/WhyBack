package byteback.mlcfg.vimp;

import byteback.mlcfg.syntax.WhyFunctionSignature;
import byteback.mlcfg.syntax.expr.Expression;
import byteback.mlcfg.syntax.WhyFunction;
import byteback.mlcfg.vimp.expr.FunctionBodyExtractor;
import soot.SootMethod;

public class VimpMethodBodyParser {
	private final FunctionBodyExtractor functionBodyExtractor;

	public VimpMethodBodyParser(FunctionBodyExtractor functionBodyExtractor) {
		this.functionBodyExtractor = functionBodyExtractor;
	}

	public WhyFunction parse(WhyFunctionSignature signature, SootMethod method) {
		final Expression body = functionBodyExtractor.visit(method.retrieveActiveBody());
		return new WhyFunction(signature, body);
	}
}
