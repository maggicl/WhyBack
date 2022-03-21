package byteback.frontend.boogie.ast;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TypeDefinitionTest extends ASTTestFixture {

    @Test
    public void TypeDefinitions_OnUnitProgram_ReturnsOneElementTable() {
        final Program program = getProgram("Unit");
        assertEquals(program.typeDefinitions().size(), 1);
    }

    @Test
    public void TypeDefinitions_OnArithmeticProgram_ReturnsZeroElementTable() {
        final Program program = getProgram("Arithmetic");
        assertEquals(program.typeDefinitions().size(), 0);
    }

    @Test
    public void getDefinedType_OnUnitUnitType_DoesNotThrowException() {
        final TypeDefinition typeDefinition = getTypeDefinition("Unit", "Unit");
        typeDefinition.getDefinedType();
    }

}
