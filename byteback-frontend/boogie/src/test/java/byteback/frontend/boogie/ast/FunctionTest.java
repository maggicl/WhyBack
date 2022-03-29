package byteback.frontend.boogie.ast;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class FunctionTest extends ASTTestFixture {

	@Test
	public void Functions_OnSimpleProgram_ReturnsOneElementTable() {
		final Program program = getProgram("Simple");
		assertTrue(program.functions().size() == 1);
	}

	@Test
	public void Functions_OnArithmeticProgram_ReturnsOneElementTable() {
		final Program program = getProgram("Arithmetic");
		assertTrue(program.functions().size() == 1);
	}

	@Test
	public void LookupFunction_OnArithmeticAdditionFunction_ReturnsFunctionDeclaration() {
		final Program program = getProgram("Arithmetic");
		assertTrue(program.lookupFunction("addition").isPresent());
	}

	@Test
	public void GetFunctionDeclaration_OnArithmeticAdditionFunction_DoesNotThrowException() {
		final Program program = getProgram("Arithmetic");
		final Function function = program.lookupFunction("addition").get();
		function.getFunctionDeclaration();
	}

	@Test
	public void GetValueReference_OnArithmeticAdditionFunction_DoesNotThrowException() {
		final Program program = getProgram("Arithmetic");
		final Function function = program.lookupFunction("addition").get();
		function.getFunctionReference();
	}

}
