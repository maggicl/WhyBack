package byteback.analysis.transformer;

import static byteback.analysis.transformer.ValueTransformerFixture.assertEquiv;
import static org.junit.Assert.assertEquals;

import byteback.analysis.Vimp;
import byteback.analysis.vimp.LogicConstant;
import org.junit.Test;
import soot.BooleanType;
import soot.Local;
import soot.Value;
import soot.ValueBox;
import soot.jimple.AssignStmt;
import soot.jimple.IntConstant;
import soot.jimple.Jimple;

public class LogicValueTransformerIntegrationTest {

	private static final LogicValueTransformer transformer = LogicValueTransformer.v();

	@Test
	public void TransformValue_GivenFalseIntConstantBox_YieldsFalse() {
		final IntConstant intConstant = IntConstant.v(0);
		final ValueBox valueBox = Jimple.v().newImmediateBox(intConstant);
		transformer.transformValue(valueBox);
		assertEquals(LogicConstant.v(false), valueBox.getValue());
	}

	@Test
	public void TransformValue_GivenTrueIntConstantBox_YieldsFalse() {
		final IntConstant intConstant = IntConstant.v(1);
		final ValueBox valueBox = Jimple.v().newImmediateBox(intConstant);
		transformer.transformValue(valueBox);
		assertEquals(LogicConstant.v(true), valueBox.getValue());
	}

	@Test
	public void TransformValue_GivenBooleanAndBox_YieldsLogicAnd() {
		final Value booleanAnd = Jimple.v().newAndExpr(LogicConstant.v(true), LogicConstant.v(false));
		final Value logicAnd = Vimp.v().newLogicAndExpr(LogicConstant.v(true), LogicConstant.v(false));
		final ValueBox valueBox = Jimple.v().newRValueBox(booleanAnd);
		transformer.transformValue(valueBox);
		assertEquiv(logicAnd, valueBox.getValue());
	}

	@Test
	public void TransformValue_GivenIntegerAndBox_YieldsLogicAnd() {
		final Value intAnd = Jimple.v().newAndExpr(IntConstant.v(0), IntConstant.v(1));
		final Value logicAnd = Vimp.v().newLogicAndExpr(IntConstant.v(0), IntConstant.v(1));
		final ValueBox valueBox = Jimple.v().newRValueBox(intAnd);
		transformer.transformValue(valueBox);
		assertEquiv(logicAnd, valueBox.getValue());
	}

	@Test
	public void TransformValue_GivenBooleanOrBox_YieldsLogicOr() {
		final Value booleanOr = Jimple.v().newOrExpr(LogicConstant.v(true), LogicConstant.v(false));
		final Value logicOr = Vimp.v().newLogicOrExpr(LogicConstant.v(true), LogicConstant.v(false));
		final ValueBox valueBox = Jimple.v().newRValueBox(booleanOr);
		transformer.transformValue(valueBox);
		assertEquiv(logicOr, valueBox.getValue());
	}

	@Test
	public void TransformValue_GivenIntegerOrBox_YieldsLogicOr() {
		final Value intAnd = Jimple.v().newOrExpr(IntConstant.v(0), IntConstant.v(1));
		final Value logicOr = Vimp.v().newLogicOrExpr(IntConstant.v(0), IntConstant.v(1));
		final ValueBox valueBox = Jimple.v().newRValueBox(intAnd);
		transformer.transformValue(valueBox);
		assertEquiv(logicOr, valueBox.getValue());
	}

	@Test
	public void TransformValue_GivenIntegerNegBox_YieldsLogicNot() {
		final Value intNeg = Jimple.v().newNegExpr(IntConstant.v(1));
		final Value logicNot = Vimp.v().newLogicNotExpr(IntConstant.v(1));
		final ValueBox valueBox = Jimple.v().newRValueBox(intNeg);
		transformer.transformValue(valueBox);
		assertEquiv(logicNot, valueBox.getValue());
	}

	@Test
	public void TransformValue_GivenBooleanNegBox_YieldsLogicNot() {
		final Value booleanNeg = Jimple.v().newNegExpr(LogicConstant.v(true));
		final Value logicNot = Vimp.v().newLogicNotExpr(LogicConstant.v(true));
		final ValueBox valueBox = Jimple.v().newRValueBox(booleanNeg);
		transformer.transformValue(valueBox);
		assertEquiv(logicNot, valueBox.getValue());
	}

	@Test
	public void TransformStatement_GivenIntConstantAssignStmt_YieldsLogicConstantAssignStmt() {
		final Local local = Jimple.v().newLocal("l", BooleanType.v());
		final AssignStmt transformed = Jimple.v().newAssignStmt(local, IntConstant.v(1));
		final AssignStmt expected = Jimple.v().newAssignStmt(local, LogicConstant.v(true));
		transformer.transformUnit(transformed);
		assertEquiv(expected.getRightOp(), transformed.getRightOp());
	}

}
