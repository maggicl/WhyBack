package byteback.frontend.boogie.ast;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ProcedureTest extends ASTTestFixture {

    @Test
    public void Procedures_OnUnitProgram_Returns3ElementTable() {
        final Program program = getProgram("Unit");
        assertTrue(program.procedures().size() == 3);
    }

    @Test
    public void Procedures_OnArithmeticProgram_ReturnsOneElementTable() {
        final Program program = getProgram("Arithmetic");
        assertTrue(program.procedures().size() == 1);
    }
    
}
