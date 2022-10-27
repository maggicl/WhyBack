package byteback.analysis.transformer;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import org.junit.Test;
import soot.Value;
import soot.jimple.InvokeExpr;
import soot.jimple.NewArrayExpr;
import soot.jimple.NewExpr;

public class FoldingTransformerIntegrationTest {

	// public Body makeBody(final Unit... units) {
	// final Body body = Jimple.v().newBody();

	// for (Unit unit : units) {
	// body.getUnits().add(unit);
	// }

	// return body;
	// }

	public FoldingTransformer getTransformer() {
		return new FoldingTransformer();
	}

	@Test
	public void HasSideEffects_OnInvokeExpr_ReturnsTrue() {
		final InvokeExpr invokeValue = mock(InvokeExpr.class);
		assertTrue(FoldingTransformer.hasSideEffects(invokeValue));
	}

	@Test
	public void HasSideEffects_OnNewExpr_ReturnsTrue() {
		final NewExpr newValue = mock(NewExpr.class);
		assertTrue(FoldingTransformer.hasSideEffects(newValue));
	}

	@Test
	public void HasSideEffects_OnNewArrayExpr_ReturnsTrue() {
		final NewArrayExpr newArrayValue = mock(NewArrayExpr.class);
		assertTrue(FoldingTransformer.hasSideEffects(newArrayValue));
	}

	@Test
	public void HasSideEffects_OnGeneralValue_ReturnsFalse() {
		final Value value = mock(Value.class);
		assertFalse(FoldingTransformer.hasSideEffects(value));
	}

}
