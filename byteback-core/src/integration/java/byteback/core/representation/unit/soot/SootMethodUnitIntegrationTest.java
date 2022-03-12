package byteback.core.representation.unit.soot;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class SootMethodUnitIntegrationTest extends SootMethodUnitFixture {

    @Test
    public void GetName_GivenIdentityIdentityMethod_ReturnsMatchingName() {
        final SootMethodUnit methodUnit = getMethodUnit("java8", "byteback.dummy.Identity",
                "identity(java.lang.Object)");
        assertEquals("identity", methodUnit.getName());
    }

    @Test
    public void GetIdentifier_GivenIdentityIdentityMethod_ReturnsMatchingIdentifier() {
        final String identifier = "identity(java.lang.Object)";
        final SootMethodUnit methodUnit = getMethodUnit("java8", "byteback.dummy.Identity", identifier);
        assertEquals(identifier, methodUnit.getIdentifier());
    }

    @Test
    public void GetBody_GivenIdentityIdentityMethod_ReturnsTwoStatementsBody() {
        final SootMethodUnit methodUnit = getMethodUnit("java8", "byteback.dummy.Identity",
                "identity(java.lang.Object)");
        assertEquals(2, methodUnit.getBody().getUnits().size());
    }

    @Test
    public void GetAnnotations_GivenIntegerMethodsIsEven_ReturnsPureAnnotation() {
        final SootMethodUnit methodUnit = getMethodUnit("java8", "byteback.dummy.IntegerMethods", "isEven(int)");
        assertEquals(1, methodUnit.getAnnotations().size());
        assertEquals(methodUnit.getAnnotations().get(0).getName(), "Pure");
    }

}
