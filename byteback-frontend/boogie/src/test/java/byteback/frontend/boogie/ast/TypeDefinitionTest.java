package byteback.frontend.boogie.ast;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class TypeDefinitionTest extends ASTTestFixture {

	@Test
	public void TypeDefinitions_OnSimpleProgram_ReturnsOneElementTable() {
		final Program program = getProgram("Simple");
		assertEquals(program.typeDefinitions().size(), 1);
	}

	@Test
	public void TypeDefinitions_OnArithmeticProgram_ReturnsZeroElementTable() {
		final Program program = getProgram("Arithmetic");
		assertEquals(program.typeDefinitions().size(), 0);
	}

	@Test
	public void getDefinedType_OnSimpleUnitType_DoesNotThrowException() {
		final TypeDefinition typeDefinition = getTypeDefinition("Simple", "Unit");
		typeDefinition.getDefinedType();
	}

}
