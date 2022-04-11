package byteback.core.converter.soottoboogie;

import static org.junit.Assert.assertEquals;

import byteback.core.RegressionParameter;
import byteback.core.converter.soottoboogie.field.FieldConverter;
import byteback.core.representation.soot.unit.SootClass;
import byteback.frontend.boogie.ast.ConstantDeclaration;
import byteback.frontend.boogie.ast.PrintUtil;
import byteback.frontend.boogie.ast.Program;
import byteback.frontend.boogie.ast.Variable;
import java.io.IOException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class FieldConverterIntegrationTest extends ConverterFixture {

	@Parameters
	public static Iterable<RegressionParameter<ConstantDeclaration>> getParameters() throws IOException {
		return getRegressionEntries("java8").flatMap((entry) -> {
			final SootClass clazz = entry.getKey();
			final Program program = entry.getValue();

			return clazz.fields().map((field) -> {
				final String name = NameConverter.fieldName(field);
				final ConstantDeclaration expected = program.lookupVariable(name)
						.flatMap(Variable::getConstantDeclaration).get();
				final ConstantDeclaration actual = FieldConverter.instance().convert(field);

				return new RegressionParameter<>(expected, actual);
			});
		})::iterator;
	}

	private final RegressionParameter<ConstantDeclaration> parameter;

	public FieldConverterIntegrationTest(final RegressionParameter<ConstantDeclaration> parameter) {
		this.parameter = parameter;
	}

	@Test
	public void Convert_GivenRegressionSet_ReturnsExpectedCode() {
		assertEquals(PrintUtil.toString(parameter.expected), PrintUtil.toString(parameter.actual));
	}

}
