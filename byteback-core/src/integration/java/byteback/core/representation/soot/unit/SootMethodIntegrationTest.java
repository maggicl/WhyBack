package byteback.core.representation.soot.unit;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SootMethodIntegrationTest extends SootMethodFixture {

	@Test
	public void GetName_GivenUnitIdentityMethod_ReturnsMatchingName() {
		final SootMethod methodUnit = getMethodUnit("java8", "byteback.dummy.context.Unit",
				"identity(java.lang.Object)");
		assertEquals("identity", methodUnit.getName());
	}

	@Test
	public void GetIdentifier_GivenUnitIdentityMethod_ReturnsMatchingIdentifier() {
		final String identifier = "identity(java.lang.Object)";
		final SootMethod methodUnit = getMethodUnit("java8", "byteback.dummy.context.Unit", identifier);
		assertEquals(identifier, methodUnit.getIdentifier());
	}

	@Test
	public void GetBody_GivenUnitIdentityMethod_ReturnsTwoStatementsBody() {
		final SootMethod methodUnit = getMethodUnit("java8", "byteback.dummy.context.Unit",
				"identity(java.lang.Object)");
		assertEquals(2, methodUnit.getBody().getStatementCount());
	}

}
