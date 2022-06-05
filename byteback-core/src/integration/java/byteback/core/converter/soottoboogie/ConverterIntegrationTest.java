package byteback.core.converter.soottoboogie;

import static org.junit.Assert.assertEquals;

import byteback.core.Parameter;
import byteback.frontend.boogie.ast.Function;
import byteback.frontend.boogie.ast.Procedure;
import byteback.frontend.boogie.ast.Program;
import byteback.frontend.boogie.ast.Variable;
import java.io.IOException;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ConverterIntegrationTest extends ConverterFixture {

	@AfterClass
	public static void after() {
		resetContext();
	}

	@Parameters
	public static Iterable<Parameter<Program>> parameters() throws IOException {
		return ConverterFixture.parameters("java8")::iterator;
	}

	private final Parameter<Program> parameter;

	public ConverterIntegrationTest(final Parameter<Program> parameter) {
		this.parameter = parameter;
	}

	@Test
	public void Convert_GivenValidInputClass_ReturnsExpectedProcedures() {
		for (Procedure expected : parameter.expected.procedures()) {
			final Procedure actual = parameter.actual.lookupProcedure(expected.getName())
					.orElseThrow(() -> new IllegalStateException(
							"Generated code does not present a procedure declaration for " + expected.getName()));
			System.out.println(actual.getName());
			assertEquals(expected.getDeclaration().print(), actual.getDeclaration().print());
		}
	}

	@Test
	public void Convert_GivenValidInputClass_ReturnsExpectedFunctions() {
		for (Function expected : parameter.expected.functions()) {
			final Function actual = parameter.actual.lookupFunction(expected.getName())
					.orElseThrow(() -> new IllegalStateException(
							"Generated code does not present a function declaration for " + expected.getName()));
			assertEquals(expected.getDeclaration().print(), actual.getDeclaration().print());
		}
	}

	@Test
	public void Convert_GivenValidInputClass_ReturnsExpectedVariables() {
		for (Variable expected : parameter.expected.variables()) {
			final Variable actual = parameter.actual.lookupLocalVariable(expected.getName())
					.orElseThrow(() -> new IllegalStateException(
							"Generated code does not present a variable declaration for " + expected.getName()));
			assertEquals(expected.getDeclaration().print(), actual.getDeclaration().print());
		}
	}

}
