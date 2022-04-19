package byteback.core.converter.soottoboogie.field;

import static org.junit.Assert.assertEquals;

import byteback.core.RegressionParameter;
import byteback.core.converter.soottoboogie.ConverterFixture;
import byteback.core.converter.soottoboogie.NameConverter;
import byteback.core.representation.soot.unit.SootClass;
import byteback.frontend.boogie.ast.ConstantDeclaration;
import byteback.frontend.boogie.ast.PrintUtil;
import byteback.frontend.boogie.ast.Program;
import byteback.frontend.boogie.ast.Variable;
import java.io.IOException;
import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class FieldConverterIntegrationTest extends ConverterFixture {

	@AfterClass
	public static void before() {
		resetContext();
	}

	@Parameters
	public static Iterable<RegressionParameter<ConstantDeclaration>> getParameters() throws IOException {
		return getRegressionEntries("java8").flatMap((entry) -> {
			final SootClass clazz = entry.getKey();
			final Program program = entry.getValue();

			return clazz.fields().map((field) -> {
				final String name = NameConverter.fieldName(field);
				final ConstantDeclaration expected = program.lookupLocalVariable(name)
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