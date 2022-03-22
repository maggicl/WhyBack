package byteback.core.converter.soot.boogie;

import static org.junit.Assert.assertEquals;

import byteback.core.RegressionParameter;
import byteback.core.representation.soot.unit.SootClassUnit;
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
public class BoogieFieldConverterIntegrationTest extends BoogieConverterFixture {

    @Parameters
    public static Iterable<RegressionParameter<ConstantDeclaration>> getParameters() throws IOException {
        return getRegressionEntries("java8").flatMap((entry) -> {
            final SootClassUnit classUnit = entry.getKey();
            final Program program = entry.getValue();

            return classUnit.fields().map((fieldUnit) -> {
                final String boogieName = BoogieNameConverter.fieldName(fieldUnit);
                final ConstantDeclaration expected = program.lookupVariable(boogieName)
                        .flatMap(Variable::getConstantDeclaration).get();
                final ConstantDeclaration actual = BoogieFieldConverter.instance().convert(fieldUnit);

                return new RegressionParameter<>(expected, actual);
            });
        })::iterator;
    }

    private final RegressionParameter<ConstantDeclaration> parameter;

    public BoogieFieldConverterIntegrationTest(final RegressionParameter<ConstantDeclaration> parameter) {
        this.parameter = parameter;
    }

    @Test
    public void Convert_GivenRegressionSet_ReturnsExpectedCode() {
        assertEquals(PrintUtil.toString(parameter.actual), PrintUtil.toString(parameter.expected));
    }

}
