package byteback.core.converter.soot.boogie;

import static org.junit.Assert.assertEquals;

import byteback.core.RegressionParameter;
import byteback.core.representation.soot.unit.SootClassUnit;
import byteback.frontend.boogie.ast.FunctionDeclaration;
import byteback.frontend.boogie.ast.PrintUtil;
import byteback.frontend.boogie.ast.Program;
import java.io.IOException;
import java.util.stream.Stream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class FunctionConverterIntegrationTest extends ConverterFixture {

	@Parameters
	public static Iterable<RegressionParameter<FunctionDeclaration>> getParameters() throws IOException {
		return getRegressionEntries("java8").flatMap((entry) -> {
			final SootClassUnit classUnit = entry.getKey();
			final Program program = entry.getValue();

			return classUnit.methods().flatMap((methodUnit) -> {
				if (methodUnit.getAnnotation("Lbyteback/annotations/Contract$Pure;").isPresent()) {
					final String boogieName = NameConverter.methodName(methodUnit);
					final FunctionDeclaration expected = program.lookupFunction(boogieName).get()
							.getFunctionDeclaration();
					final FunctionDeclaration actual = FunctionConverter.instance().convert(methodUnit);

					return Stream.of(new RegressionParameter<>(expected, actual));
				}

				return Stream.empty();
			});
		})::iterator;
	}

	private final RegressionParameter<FunctionDeclaration> parameter;

	public FunctionConverterIntegrationTest(final RegressionParameter<FunctionDeclaration> parameter) {
		this.parameter = parameter;
	}

	@Test
	public void Convert_GivenRegressionSet_ReturnsExpectedCode() {
		assertEquals(PrintUtil.toString(parameter.expected), PrintUtil.toString(parameter.actual));
	}

}