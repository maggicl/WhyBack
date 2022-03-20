package byteback.core.converter.soot.boogie;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.stream.Stream;

import byteback.core.RegressionParameter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import byteback.core.representation.soot.unit.SootClassUnit;
import byteback.frontend.boogie.ast.FunctionDeclaration;
import byteback.frontend.boogie.ast.PrintUtil;
import byteback.frontend.boogie.ast.Program;

@RunWith(Parameterized.class)
public class BoogieFunctionExtractorIntegrationTest extends BoogieFunctionExtractorFixture {

    @Parameters
    public static Iterable<RegressionParameter<FunctionDeclaration>> getParameters() throws IOException {
        return getRegressionEntries("java8").flatMap((entry) -> {
            final SootClassUnit classUnit = entry.getKey();
            final Program program = entry.getValue();

            return classUnit.methods().flatMap((methodUnit) -> {
                if (methodUnit.getAnnotation("Lbyteback/annotations/Contract$Pure;").isPresent()) {
                    final String boogieName = BoogieNameConverter.methodName(methodUnit);
                    final FunctionDeclaration expected = program.lookupFunction(boogieName).get()
                            .getFunctionDeclaration();
                    final FunctionDeclaration actual = new BoogieFunctionExtractor(methodUnit).convert();

                    return Stream.of(new RegressionParameter<>(expected, actual));
                }
                return Stream.empty();
            });
        })::iterator;
    }

    private final RegressionParameter<FunctionDeclaration> parameter;

    public BoogieFunctionExtractorIntegrationTest(final RegressionParameter<FunctionDeclaration> parameter) {
        this.parameter = parameter;
    }

    @Test
    public void test() {
        assertEquals(PrintUtil.toString(parameter.actual), PrintUtil.toString(parameter.expected));
    }

}
