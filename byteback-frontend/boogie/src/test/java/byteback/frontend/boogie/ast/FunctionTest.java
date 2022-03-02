package byteback.frontend.boogie.ast;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FunctionTest extends ASTTestFixture {

    @Test
    public void Functions_OnUnitProgram_ReturnsOneElementTable() {
        final Program program = getProgram("Unit");
        assertTrue(program.functions().size() == 1);
    }

    @Test
    public void Functions_OnArithmeticProgram_ReturnsOneElementTable() {
        final Program program = getProgram("Arithmetic");
        assertTrue(program.functions().size() == 1);
    }

}
