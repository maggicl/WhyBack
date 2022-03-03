package byteback.frontend.boogie.ast;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class TypeDefinitionTest extends ASTTestFixture {

    @Test
    public void TypeDefinitions_OnUnitProgram_ReturnsOneElementTable() {
        final Program program = getProgram("Unit");
        assertTrue(program.typeDefinitions().size() == 1);
    }

    @Test
    public void TypeDefinitions_OnArithmeticProgram_ReturnsZeroElementTable() {
        final Program program = getProgram("Arithmetic");
        assertTrue(program.typeDefinitions().size() == 0);
    }

}
