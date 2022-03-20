package byteback.frontend.boogie.ast;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

import org.junit.Test;

public class VariableTest extends ASTTestFixture {

    @Test
    public void Variables_OnUnitProgram_ReturnsSingleElementTable() {
        final Program program = getProgram("Unit");
        assertEquals(1, program.variables().size());
    }

    @Test
    public void Variables_OnUnitIdentityFunction_ReturnsSingleElementTable() {
        final Function function = getFunction("Unit", "identity");
        assertEquals(1, function.variables().size());
    }

    @Test
    public void Variables_OnArithmeticAdditionFunction_Returns2ElementsTable() {
        final Function function = getFunction("Arithmetic", "addition");
        assertEquals(2, function.variables().size());
    }

    @Test
    public void Variables_OnArithmeticSumProcedure_Returns2ElementsTable() {
        final Procedure procedure = getProcedure("Arithmetic", "sum");
        assertEquals(2, procedure.variables().size());
    }

    @Test
    public void Variables_OnArithmeticIdentityProcedure_Returns2ElementsTable() {
        final Procedure procedure = getProcedure("Unit", "identity");
        assertEquals(2, procedure.variables().size());
    }

    @Test
    public void Variables_OnArithmeticIdentityProcedureBody_Returns2ElementsTable() {
        final Procedure procedure = getProcedure("Unit", "identity");
        assertEquals(1, procedure.getBody().variables().size());
    }

    @Test
    public void Variables_OnUnitPrototypeImplementation_Returns2ElementsTable() {
        final Collection<Implementation> implementations = getImplementations("Unit", "prototype");

        for (Implementation implementation : implementations) {
            assertEquals(2, implementation.variables().size());
        }
    }

}
