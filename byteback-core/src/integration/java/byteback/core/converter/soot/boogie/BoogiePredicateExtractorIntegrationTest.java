package byteback.core.converter.soot.boogie;

import byteback.core.representation.unit.soot.SootMethodUnit;
import byteback.core.representation.unit.soot.SootMethodUnitFixture;
import byteback.frontend.boogie.ast.FunctionDeclaration;

import org.junit.Test;

public class BoogiePredicateExtractorIntegrationTest extends SootMethodUnitFixture {

    @Test
    public void test() {
        final SootMethodUnit methodUnit = getMethodUnit("java8", "byteback.dummy.IntegerMethods", "even(int)");
        final BoogieFunctionExtractor functionExtractor = new BoogieFunctionExtractor();
        functionExtractor.convert(methodUnit);
        final FunctionDeclaration declaration = functionExtractor.result();
        StringBuilder builder = new StringBuilder();
        declaration.print(builder);
        System.out.println(builder.toString());
    }

}
