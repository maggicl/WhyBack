package byteback.core.converter.soottoboogie;

import static org.junit.Assert.assertEquals;

import byteback.core.RegressionParameter;
import byteback.core.converter.soottoboogie.method.procedure.ProcedureConverter;
import byteback.core.representation.soot.unit.SootClass;
import byteback.frontend.boogie.ast.ConstantDeclaration;
import byteback.frontend.boogie.ast.PrintUtil;
import byteback.frontend.boogie.ast.Procedure;
import byteback.frontend.boogie.ast.ProcedureDeclaration;
import byteback.frontend.boogie.ast.Program;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ProcedureConverterIntegrationTest extends ConverterFixture {

	@AfterClass
	public static void after() {
		resetContext();
	}

	@Parameters
	public static Iterable<RegressionParameter<ProcedureDeclaration>> getParameters() throws IOException {
		return getRegressionEntries("java8").flatMap((entry) -> {
			final SootClass clazz = entry.getKey();
			final Program program = entry.getValue();

			return clazz.methods().flatMap((method) -> {
				if (!method.getAnnotation(Annotations.PURE_ANNOTATION).isPresent()
						&& !method.getAnnotation(Annotations.CONDITION_ANNOTATION).isPresent()) {
					final String name = NameConverter.methodName(method);
					final Optional<ProcedureDeclaration> expected = program.lookupProcedure(name)
							.map(Procedure::getProcedureDeclaration);

					if (expected.isPresent()) {
						final ProcedureDeclaration actual = ProcedureConverter.instance().convert(method);

						return Stream.of(new RegressionParameter<>(expected.get(), actual));
					}
				}

				return Stream.empty();
			});
		})::iterator;
	}

	private final RegressionParameter<ConstantDeclaration> parameter;

	public ProcedureConverterIntegrationTest(final RegressionParameter<ConstantDeclaration> parameter) {
		this.parameter = parameter;
	}

	@Test
	public void Convert_GivenRegressionSet_ReturnsExpectedCode() {
    System.err.println(PrintUtil.toString(parameter.actual));
		assertEquals(PrintUtil.toString(parameter.expected), PrintUtil.toString(parameter.actual));
	}

}
