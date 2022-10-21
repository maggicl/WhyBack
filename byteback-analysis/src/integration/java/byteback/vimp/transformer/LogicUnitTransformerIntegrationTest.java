package byteback.vimp.transformer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static byteback.vimp.transformer.UnitTransformerFixture.assertLogicUnitEquiv;

import org.junit.Test;

import byteback.core.converter.soottoboogie.Namespace;
import byteback.vimp.Vimp;
import byteback.vimp.internal.LogicConstant;
import soot.Unit;
import soot.UnitBox;
import soot.Value;
import soot.jimple.ConditionExpr;
import soot.jimple.IfStmt;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.StaticInvokeExpr;

public class LogicUnitTransformerIntegrationTest {

	private static final LogicUnitTransformer transformer = LogicUnitTransformer.v();

	private static final LogicConstant trueConstant = LogicConstant.v(true);

	public static StaticInvokeExpr mockLogicStmtRef(final String methodName, final Value value) {
		return TransformerFixture.mockStaticInvokeExpr(Namespace.CONTRACT_CLASS_NAME, methodName, new Value[]{ value });
	}

	@Test
	public void TransformUnit_GivenAssertionMethodRef_YieldsAssertionStmt() {
		final StaticInvokeExpr assertionRef = mockLogicStmtRef(Namespace.ASSERTION_NAME, trueConstant);
		final InvokeStmt invokeUnit = Jimple.v().newInvokeStmt(assertionRef);
		final UnitBox unitBox = Jimple.v().newStmtBox(invokeUnit);
		transformer.transformUnit(unitBox);
		final Unit transformedUnit = unitBox.getUnit();
		final Unit expectedunit = Vimp.v().newAssertionStmt(trueConstant);
		assertLogicUnitEquiv(expectedunit, transformedUnit);
	}

	@Test
	public void TransformUnit_GivenAssumptionMethodRef_YieldsAssumptionStmt() {
		final StaticInvokeExpr assumptionRef = mockLogicStmtRef(Namespace.ASSUMPTION_NAME, trueConstant);
		final InvokeStmt invokeUnit = Jimple.v().newInvokeStmt(assumptionRef);
		final UnitBox unitBox = Jimple.v().newStmtBox(invokeUnit);
		transformer.transformUnit(unitBox);
		final Unit transformedUnit = unitBox.getUnit();
		final Unit expectedunit = Vimp.v().newAssumptionStmt(trueConstant);
		assertLogicUnitEquiv(expectedunit, transformedUnit);
	}

	@Test
	public void TransformUnit_GivenInvariantMethodRef_YieldsInvariantStmt() {
		final StaticInvokeExpr invariantRef = mockLogicStmtRef(Namespace.INVARIANT_NAME, trueConstant);
		final InvokeStmt invokeUnit = Jimple.v().newInvokeStmt(invariantRef);
		final UnitBox unitBox = Jimple.v().newStmtBox(invokeUnit);
		transformer.transformUnit(unitBox);
		final Unit transformedUnit = unitBox.getUnit();
		final Unit expectedunit = Vimp.v().newInvariantStmt(trueConstant);
		assertLogicUnitEquiv(expectedunit, transformedUnit);
	}

	@Test
	public void TransformUnit_GivenAssertionMethodRef_PreservesBranchesToStmt() {
		final StaticInvokeExpr assertionRef = mockLogicStmtRef(Namespace.ASSERTION_NAME, trueConstant);
		final InvokeStmt invokeUnit = Jimple.v().newInvokeStmt(assertionRef);
		final ConditionExpr condValue = mock(ConditionExpr.class);
		final IfStmt ifUnit = Jimple.v().newIfStmt(condValue, invokeUnit);
		final UnitBox unitBox = Jimple.v().newStmtBox(invokeUnit);
		transformer.transformUnit(unitBox);
		final Unit transformedUnit = unitBox.getUnit();
		assertEquals(ifUnit.getTarget(), transformedUnit);
	}

}
