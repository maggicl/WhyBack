package byteback.core.converter.soot.boogie;

import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.Map.Entry;

import org.junit.Test;

import byteback.core.representation.unit.soot.SootMethodUnit;
import byteback.frontend.boogie.ast.FunctionDeclaration;

public class BoogieFunctionExtractorIntegrationTest extends BoogieFunctionExtractorFixture {

    @Test
    public void Convert_GivenRegressedFunction_ReturnsExpectedResult() {
        final Map<SootMethodUnit, FunctionDeclaration> regressionSet = getFunctionRegressionSet("java8");

        for (Entry<SootMethodUnit, FunctionDeclaration> pair : regressionSet.entrySet()) {
            final SootMethodUnit methodUnit = pair.getKey();
            final FunctionDeclaration expected = pair.getValue();
            final FunctionDeclaration actual = new BoogieFunctionExtractor().convert(methodUnit);
            StringBuilder expectedBuilder = new StringBuilder();
            StringBuilder actualBuilder = new StringBuilder();
            expected.print(expectedBuilder);
            actual.print(actualBuilder);
            assertEquals(expectedBuilder.toString(), actualBuilder.toString());
        }
    }

}
