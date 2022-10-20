package byteback.vimp.transformer;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import byteback.core.converter.soottoboogie.Namespace;
import byteback.vimp.Vimp;
import byteback.vimp.internal.LogicConstant;
import byteback.vimp.internal.LogicStmt;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.UnitBox;
import soot.Value;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.StaticInvokeExpr;

public class LogicUnitTransformerIntegrationTest {

	private static final LogicUnitTransformer transformer = LogicUnitTransformer.v();

	public static StaticInvokeExpr mockStaticInvokeExpr(final String name, final Value value) {
		final SootClass clazz = mock(SootClass.class);
		when(clazz.getName()).thenReturn(Namespace.CONTRACT_CLASS_NAME);
		final SootMethod method = mock(SootMethod.class); 
		when(method.getName()).thenReturn(name);
		when(method.getDeclaringClass()).thenReturn(clazz);
		final StaticInvokeExpr invokeValue = mock(StaticInvokeExpr.class);
		when(invokeValue.getArgCount()).thenReturn(1);
		when(invokeValue.getArg(0)).thenReturn(value);
		when(invokeValue.getMethod()).thenReturn(method);

		return invokeValue;
	}

	public static void assertUnitEquiv(final Unit a, final Unit b) {
		if (a instanceof LogicStmt al) {
			if (b instanceof LogicStmt bl) {
				if (al.getClass() != bl.getClass()) {
					fail("Comparing different types of logic statements " + a.getClass() + " and " + b.getClass());
				}
				if (!al.getCondition().equivTo(bl.getCondition())) {
					fail("Conditions " + al.getCondition() + " and " + bl.getCondition() + " are not equivalent");
				}
			} else {
				fail("Second argument must be a logic statement");
			}
		} else {
			fail("First argument must be a logic statement");
		}
	}

	@Test
	public void TransformUnit_GivenAssertionMethodReference_YieldsAssertionStmt() {
		final LogicConstant trueConstant = LogicConstant.v(true);
		final StaticInvokeExpr assertionReference = mockStaticInvokeExpr(Namespace.ASSERTION_NAME, trueConstant);
		final InvokeStmt invokeUnit = Jimple.v().newInvokeStmt(assertionReference);
		final UnitBox unitBox = Jimple.v().newStmtBox(invokeUnit);
		transformer.transformUnit(unitBox);
		final Unit transformedUnit = unitBox.getUnit();
		final Unit expectedunit = Vimp.v().newAssertionStmt(trueConstant);
		assertUnitEquiv(expectedunit, transformedUnit);
	}

	@Test
	public void TransformUnit_GivenAssumptionMethodReference_YieldsAssumptionStmt() {
		final LogicConstant trueConstant = LogicConstant.v(true);
		final StaticInvokeExpr assertionReference = mockStaticInvokeExpr(Namespace.ASSUMPTION_NAME, trueConstant);
		final InvokeStmt invokeUnit = Jimple.v().newInvokeStmt(assertionReference);
		final UnitBox unitBox = Jimple.v().newStmtBox(invokeUnit);
		transformer.transformUnit(unitBox);
		final Unit transformedUnit = unitBox.getUnit();
		final Unit expectedunit = Vimp.v().newAssumptionStmt(trueConstant);
		assertUnitEquiv(expectedunit, transformedUnit);
	}

	@Test
	public void TransformUnit_GivenInvariantMethodReference_YieldsInvariantStmt() {
		final LogicConstant trueConstant = LogicConstant.v(true);
		final StaticInvokeExpr assertionReference = mockStaticInvokeExpr(Namespace.INVARIANT_NAME, trueConstant);
		final InvokeStmt invokeUnit = Jimple.v().newInvokeStmt(assertionReference);
		final UnitBox unitBox = Jimple.v().newStmtBox(invokeUnit);
		transformer.transformUnit(unitBox);
		final Unit transformedUnit = unitBox.getUnit();
		final Unit expectedunit = Vimp.v().newInvariantStmt(trueConstant);
		assertUnitEquiv(expectedunit, transformedUnit);
	}

}
