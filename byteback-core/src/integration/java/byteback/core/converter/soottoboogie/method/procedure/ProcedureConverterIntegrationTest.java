package byteback.core.converter.soottoboogie.method.procedure;

import static org.junit.Assert.assertEquals;

import byteback.core.Parameter;
import byteback.core.converter.soottoboogie.Annotations;
import byteback.core.converter.soottoboogie.ConverterFixture;
import byteback.core.converter.soottoboogie.NameConverter;
import byteback.core.converter.soottoboogie.Prelude;
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
	public static Iterable<Parameter<ProcedureDeclaration>> getParameters() throws IOException {
		return getRegressionEntries("java8").flatMap((entry) -> {
			final SootClass clazz = entry.getKey();
			final Program program = entry.getValue();

			return clazz.methods().flatMap((method) -> {
				if (method.getAnnotation(Annotations.PURE_ANNOTATION).isEmpty()
						&& method.getAnnotation(Annotations.CONDITION_ANNOTATION).isEmpty()) {
					final String name = NameConverter.methodName(method);
					final Optional<ProcedureDeclaration> expected = program.lookupProcedure(name)
							.map(Procedure::getProcedureDeclaration);

					if (expected.isPresent()) {
						final ProcedureDeclaration actual = ProcedureConverter.instance().convert(method).rootedCopy();
            final Program root = actual.getProgram();

						Prelude.inject(root);
						root.removeUnusedVariables();
            root.inferModifies();

						return Stream.of(new Parameter<>(expected.get(), actual));
					}
				}

				return Stream.empty();
			});
		})::iterator;
	}

	private final Parameter<ConstantDeclaration> parameter;

	public ProcedureConverterIntegrationTest(final Parameter<ConstantDeclaration> parameter) {
		this.parameter = parameter;
	}

	@Test
	public void Convert_GivenRegressionSet_ReturnsExpectedCode() {
    assertEquals(PrintUtil.toString(parameter.expected), PrintUtil.toString(parameter.actual));
	}

}
