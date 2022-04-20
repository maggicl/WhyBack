package byteback.core.converter.soottoboogie.program;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import byteback.core.Parameter;
import byteback.core.converter.soottoboogie.ConverterFixture;
import byteback.frontend.boogie.ast.Function;
import byteback.frontend.boogie.ast.Procedure;
import byteback.frontend.boogie.ast.Program;
import byteback.frontend.boogie.ast.Variable;

@RunWith(Parameterized.class)
public class ProgramConverterIntegrationTest extends ConverterFixture {

  @AfterClass
  public static void after() {
    resetContext();
  }

  @Parameters
  public static Iterable<Parameter<Program>> getParameters() throws IOException {
    return getRegressionEntries("java8").map((entry) -> {
      final Program expected = entry.getValue();
      final Program actual = ProgramConverter.instance().convert(entry.getKey());
      return new Parameter<>(expected, actual);
    })::iterator;
  }

	private final Parameter<Program> parameter;

	public ProgramConverterIntegrationTest(final Parameter<Program> parameter) {
		this.parameter = parameter;
	}

	@Test
	public void Convert_GivenRegressionSet_ReturnsProgramsWithMatchingFunctions() {
    for (Function function : parameter.expected.functions()) {
      assertTrue(parameter.actual.lookupFunction(function.getName()).isPresent());
    }
	}

	@Test
	public void Convert_GivenRegressionSet_ReturnsProgramsWithMatchingProcedures() {
    for (Procedure procedure : parameter.expected.procedures()) {
      System.err.println(procedure.getName());
      assertTrue(parameter.actual.lookupProcedure(procedure.getName()).isPresent());
    }
	}

	@Test
	public void Convert_GivenRegressionSet_ReturnsProgramsWithMatchingVariables() {
    for (Variable variable : parameter.expected.variables()) {
      assertTrue(parameter.actual.lookupLocalVariable(variable.getName()).isPresent());
    }
	}

}
