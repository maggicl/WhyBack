package byteback.core.converter.soottoboogie.method.function;

import static org.junit.Assert.assertEquals;

import byteback.core.Parameter;
import byteback.core.converter.soottoboogie.Annotations;
import byteback.core.converter.soottoboogie.ConverterFixture;
import byteback.core.converter.soottoboogie.NameConverter;
import byteback.core.representation.soot.unit.SootClass;
import byteback.frontend.boogie.ast.FunctionDeclaration;
import byteback.frontend.boogie.ast.PrintUtil;
import byteback.frontend.boogie.ast.Program;
import java.io.IOException;
import java.util.stream.Stream;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class FunctionConverterIntegrationTest extends ConverterFixture {

	@AfterClass
	public static void after() {
		resetContext();
	}

	@Parameters
	public static Iterable<Parameter<FunctionDeclaration>> getParameters() throws IOException {
		return getRegressionEntries("java8").flatMap((entry) -> {
			final SootClass clazz = entry.getKey();
			final Program program = entry.getValue();

			return clazz.methods().flatMap((method) -> {
				if (method.getAnnotation(Annotations.PURE_ANNOTATION).isPresent()) {
					final String name = NameConverter.methodName(method);
					final FunctionDeclaration expected = program.lookupFunction(name).get().getFunctionDeclaration();
					final FunctionDeclaration actual = FunctionConverter.instance().convert(method);

					return Stream.of(new Parameter<>(expected, actual));
				}

				return Stream.empty();
			});
		})::iterator;
	}

	private final Parameter<FunctionDeclaration> parameter;

	public FunctionConverterIntegrationTest(final Parameter<FunctionDeclaration> parameter) {
		this.parameter = parameter;
	}

	@Test
	public void Convert_GivenRegressionSet_ReturnsExpectedCode() {
		assertEquals(PrintUtil.toString(parameter.expected), PrintUtil.toString(parameter.actual));
	}

}
