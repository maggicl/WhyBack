package byteback.analysis;

import byteback.analysis.vimp.LogicAndExpr;
import byteback.analysis.vimp.LogicConstant;
import byteback.analysis.vimp.LogicExistsExpr;
import byteback.analysis.vimp.LogicForallExpr;
import byteback.analysis.vimp.LogicIffExpr;
import byteback.analysis.vimp.LogicImpliesExpr;
import byteback.analysis.vimp.LogicNotExpr;
import byteback.analysis.vimp.LogicOrExpr;
import soot.Value;

public interface LogicExprVisitor {

	default void caseLogicAndExpr(final LogicAndExpr v) {
		defaultCase(v);
	}

	default void caseLogicOrExpr(final LogicOrExpr v) {
		defaultCase(v);
	}

	default void caseLogicImpliesExpr(final LogicImpliesExpr v) {
		defaultCase(v);
	}

	default void caseLogicIffExpr(final LogicIffExpr v) {
		defaultCase(v);
	}

	default void caseLogicForallExpr(final LogicForallExpr v) {
		defaultCase(v);
	}

	default void caseLogicExistsExpr(final LogicExistsExpr v) {
		defaultCase(v);
	}

	default void caseLogicNotExpr(final LogicNotExpr v) {
		defaultCase(v);
	}

	default void caseLogicConstant(final LogicConstant v) {
		defaultCase(v);
	}

	default void defaultCase(final Value v) {
	}

}
