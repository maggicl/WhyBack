package byteback.frontend.boogie.ast;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import byteback.frontend.boogie.TestUtil;

public class FunctionTest extends ASTTestFixture {

	@Test
	public void Functions_OnSimpleProgram_ReturnsOneElementTable() {
		final Program program = getProgram("Simple");
		assertEquals(1, program.functions().size());
	}

	@Test
	public void Functions_OnArithmeticProgram_ReturnsOneElementTable() {
		final Program program = getProgram("Arithmetic");
		assertEquals(1, program.functions().size());
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
	public void makeFunctionReference_OnArithmeticAdditionFunction_DoesNotThrowException() {
		final Program program = getProgram("Arithmetic");
		final Function function = program.lookupFunction("addition").get();
		function.makeFunctionReference();
	}

  @Test
  public void Inline_GivenArithmeticAddReference_ReturnsExpectedExpression() throws Exception {
		final Program program = getProgram("Arithmetic");
		final Function function = program.lookupFunction("addition").get();
    final ValueReference c = new ValueReference(new Accessor("c"));
    final ValueReference d = new ValueReference(new Accessor("d"));
    final Expression expected = new AdditionOperation(c, d);
    final Expression actual = function.inline(new List<>(c, d));
    TestUtil.assertAstEquals(expected, actual);
  }

}
