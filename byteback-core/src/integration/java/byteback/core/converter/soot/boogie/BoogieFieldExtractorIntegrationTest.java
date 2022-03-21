package byteback.core.converter.soot.boogie;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import byteback.core.RegressionParameter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import byteback.core.representation.soot.unit.SootClassUnit;
import byteback.frontend.boogie.ast.ConstantDeclaration;
import byteback.frontend.boogie.ast.PrintUtil;
import byteback.frontend.boogie.ast.Program;
import byteback.frontend.boogie.ast.Variable;

@RunWith(Parameterized.class)
public class BoogieFieldExtractorIntegrationTest extends BoogieFunctionExtractorFixture {

    @Parameters
    public static Iterable<RegressionParameter<ConstantDeclaration>> getParameters() throws IOException {
        return getRegressionEntries("java8").flatMap((entry) -> {
            final SootClassUnit classUnit = entry.getKey();
            final Program program = entry.getValue();

            return classUnit.fields().map((fieldUnit) -> {
                final String boogieName = BoogieNameConverter.fieldName(fieldUnit);
                final ConstantDeclaration expected = program.lookupVariable(boogieName)
                        .flatMap(Variable::getConstantDeclaration).get();
                final ConstantDeclaration actual = new BoogieFieldExtractor(fieldUnit).convert();
                return new RegressionParameter<>(expected, actual);
            });
        })::iterator;
    }

    private final RegressionParameter<ConstantDeclaration> parameter;

    public BoogieFieldExtractorIntegrationTest(final RegressionParameter<ConstantDeclaration> parameter) {
        this.parameter = parameter;
    }

    @Test
    public void test() {
        assertEquals(PrintUtil.toString(parameter.actual), PrintUtil.toString(parameter.expected));
    }

}
