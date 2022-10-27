package byteback.analysis.transformer;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.withSettings;

import org.junit.Test;
import soot.Local;
import soot.SootMethodRef;
import soot.Unit;
import soot.UnitBox;
import soot.Value;
import soot.ValueBox;
import soot.grimp.internal.GAssignStmt;
import soot.grimp.internal.GStaticInvokeExpr;
import soot.jimple.Jimple;

public class AggregationTransformerIntegrationTest {

	final AggregationTransformer transformer = AggregationTransformer.v();

	@Test
	public void TransformUnit_OnJimpleAssignBox_YieldsGrimpAssignBox() {
		final Value local = mock(Value.class, withSettings().extraInterfaces(Local.class));
		final Value value = mock(Value.class, withSettings().extraInterfaces(Local.class));
		final Unit assignUnit = Jimple.v().newAssignStmt(local, value);
		final UnitBox assignBox = Jimple.v().newStmtBox(assignUnit);
		transformer.transformUnit(assignBox);

		if (assignBox.getUnit()instanceof GAssignStmt newAssignUnit) {
			assertEquals(newAssignUnit.getLeftOp(), local);
			assertEquals(newAssignUnit.getRightOp(), value);
		} else {
			fail();
		}
	}

	@Test
	public void TransformValue_OnJimpleStaticInvokeValue_YieldsGrimpStaticInvokeValue() {
		final Value argument = mock(Value.class, withSettings().extraInterfaces(Local.class));
		final SootMethodRef methodRef = mock(SootMethodRef.class);
		when(methodRef.isStatic()).thenReturn(true);
		final Value invokeValue = Jimple.v().newStaticInvokeExpr(methodRef, argument);
		final ValueBox invokeBox = Jimple.v().newRValueBox(invokeValue);
		transformer.transformValue(invokeBox);

		if (invokeBox.getValue()instanceof GStaticInvokeExpr newInvokeValue) {
			assertEquals(newInvokeValue.getMethodRef(), methodRef);
			assertEquals(newInvokeValue.getArg(0), argument);
		} else {
			fail();
		}
	}

}
