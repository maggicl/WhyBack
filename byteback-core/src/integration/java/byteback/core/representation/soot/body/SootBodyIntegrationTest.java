package byteback.core.representation.soot.body;

import static org.junit.Assert.assertEquals;

import byteback.core.representation.soot.unit.SootMethod;
import byteback.core.representation.soot.unit.SootMethodFixture;
import org.junit.Test;

public class SootBodyIntegrationTest extends SootMethodFixture {

	@Test
	public void GetLoops_GivenProcedureSimpleEmpty_ReturnsCollectionOfZero() {
		final SootMethod method = getSootMethod("java8", "byteback.dummy.procedure.Simple", "empty()");
		final SootBody body = method.getBody();
		assertEquals(0, body.getLoops().size());
	}

	@Test
	public void GetLoops_GivenProcedureSimpleEmptyForBody_ReturnsCollectionOfOne() {
		final SootMethod method = getSootMethod("java8", "byteback.dummy.procedure.Simple", "emptyFor()");
		final SootBody body = method.getBody();
		assertEquals(1, body.getLoops().size());
	}

	@Test
	public void GetLoops_GivenProcedureSimpleEmptyWhileBody_ReturnsCollectionOfOne() {
		final SootMethod method = getSootMethod("java8", "byteback.dummy.procedure.Simple", "emptyWhile()");
		final SootBody body = method.getBody();
		assertEquals(1, body.getLoops().size());
	}

	@Test
	public void GetLoops_GivenProcedureSimpleEmptyDoWhileBody_ReturnsCollectionOfOne() {
		final SootMethod method = getSootMethod("java8", "byteback.dummy.procedure.Simple", "emptyDoWhile()");
		final SootBody body = method.getBody();
		assertEquals(1, body.getLoops().size());
	}

	@Test
	public void GetLoops_GivenProcedureSimpleEmptyNestedForBody_ReturnsCollectionOfOne() {
		final SootMethod method = getSootMethod("java8", "byteback.dummy.procedure.Simple", "emptyNestedFor()");
		final SootBody body = method.getBody();
		assertEquals(2, body.getLoops().size());
	}

}
