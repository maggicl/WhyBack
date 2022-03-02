package byteback.frontend.boogie.ast;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class VariableTest extends ASTTestFixture {

    @Test
    public void Variables_OnUnitProgram_ReturnsSingleElementTable() {
        final Program program = getProgram("Unit");
        assertTrue(program.variables().size() == 1);
    }

    @Test
    public void Variables_OnUnitIdentityFunction_ReturnsSingleElementTable() {
        final Function function = getFunction("Unit", "identity");
        assertTrue(function.variables().size() == 1);
    }

    @Test
    public void Variables_OnArithmeticAdditionFunction_Returns2ElementsTable() {
        final Function function = getFunction("Arithmetic", "addition");
        assertTrue(function.variables().size() == 2);
    }

    @Test
    public void Variables_OnArithmeticSumProcedure_Returns2ElementsTable() {
        final Procedure procedure = getProcedure("Arithmetic", "sum");
        assertTrue(procedure.variables().size() == 2);
    }

    @Test
    public void Variables_OnArithmeticIdentityProcedure_Returns2ElementsTable() {
        final Procedure procedure = getProcedure("Unit", "identity");
        assertTrue(procedure.variables().size() == 2);
    }

    @Test
    public void Variables_OnArithmeticIdentityProcedureBody_Returns2ElementsTable() {
        final Procedure procedure = getProcedure("Unit", "identity");
        assertTrue(procedure.getBody().variables().size() == 1);
    }
    
}
