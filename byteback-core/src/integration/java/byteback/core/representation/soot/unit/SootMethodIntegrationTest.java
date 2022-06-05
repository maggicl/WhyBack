package byteback.core.representation.soot.unit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SootMethodIntegrationTest extends SootMethodFixture {

	@Test
	public void GetName_GivenUnitIdentityMethod_ReturnsMatchingName() {
		final SootMethod method = getSootMethod("java8", "byteback.dummy.context.Unit", "identity(java.lang.Object)");
		assertEquals("identity", method.getName());
	}

	@Test
	public void GetIdentifier_GivenUnitIdentityMethod_ReturnsMatchingIdentifier() {
		final String identifier = "identity(java.lang.Object)";
		final SootMethod method = getSootMethod("java8", "byteback.dummy.context.Unit", identifier);
		assertEquals(identifier, method.getIdentifier());
	}

	@Test
	public void GetBody_GivenUnitIdentityMethod_ReturnsTwoStatementsBody() {
		final SootMethod method = getSootMethod("java8", "byteback.dummy.context.Unit", "identity(java.lang.Object)");
		assertEquals(2, method.getBody().getStatementCount());
	}

}
