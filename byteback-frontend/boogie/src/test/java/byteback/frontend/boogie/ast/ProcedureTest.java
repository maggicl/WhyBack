package byteback.frontend.boogie.ast;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ProcedureTest extends ASTTestFixture {

    @Test
    public void Procedures_OnUnitProgram_ReturnsOneElementTable() {
        final Program program = getProgram("Unit");
        assertTrue(program.procedures().size() == 1);
    }

    @Test
    public void Procedures_OnArithmeticProgram_ReturnsOneElementTable() {
        final Program program = getProgram("Arithmetic");
        assertTrue(program.procedures().size() == 1);
    }
    
}
