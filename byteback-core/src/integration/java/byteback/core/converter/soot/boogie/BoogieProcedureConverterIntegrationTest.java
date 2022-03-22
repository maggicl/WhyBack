package byteback.core.converter.soot.boogie;

import static org.junit.Assert.assertEquals;

import byteback.core.RegressionParameter;
import byteback.core.representation.soot.unit.SootClassUnit;
import byteback.frontend.boogie.ast.ConstantDeclaration;
import byteback.frontend.boogie.ast.PrintUtil;
import byteback.frontend.boogie.ast.Procedure;
import byteback.frontend.boogie.ast.ProcedureDeclaration;
import byteback.frontend.boogie.ast.Program;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Stream;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RunWith(Parameterized.class)
public class BoogieProcedureConverterIntegrationTest extends BoogieConverterFixture {

	final private static Logger log = LoggerFactory.getLogger(BoogieProcedureConverterIntegrationTest.class);

	@Parameters
	public static Iterable<RegressionParameter<ProcedureDeclaration>> getParameters() throws IOException {
		return getRegressionEntries("java8").flatMap((entry) -> {
			final SootClassUnit classUnit = entry.getKey();
			final Program program = entry.getValue();

			return classUnit.methods().flatMap((methodUnit) -> {
				final String boogieName = BoogieNameConverter.methodName(methodUnit);
				final Optional<ProcedureDeclaration> expected = program.lookupProcedure(boogieName)
						.map(Procedure::getProcedureDeclaration);

				if (expected.isPresent()) {
					final ProcedureDeclaration actual = BoogieProcedureConverter.instance().convert(methodUnit);

					return Stream.of(new RegressionParameter<>(expected.get(), actual));
				} else {
					log.info("Skipping " + methodUnit.getName());
					return Stream.empty();
				}
			});
		})::iterator;
	}

	private final RegressionParameter<ConstantDeclaration> parameter;

	public BoogieProcedureConverterIntegrationTest(final RegressionParameter<ConstantDeclaration> parameter) {
		this.parameter = parameter;
	}

	@Test
	public void Convert_GivenRegressionSet_ReturnsExpectedCode() {
		assertEquals(PrintUtil.toString(parameter.actual), PrintUtil.toString(parameter.expected));
	}

}
