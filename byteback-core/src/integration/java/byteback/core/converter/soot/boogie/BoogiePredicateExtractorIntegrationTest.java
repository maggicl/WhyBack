package byteback.core.converter.soot.boogie;

import byteback.core.representation.unit.soot.SootClassUnitFixture;
import byteback.frontend.boogie.ast.FunctionDeclaration;

import org.junit.Test;

import byteback.core.representation.unit.soot.SootClassUnit;

public class BoogiePredicateExtractorIntegrationTest extends SootClassUnitFixture {

    @Test
    public void test() {
        final String integerPredicatesName = "byteback.dummy.IntegerMethods";
        final SootClassUnit classUnit = getClassUnit("java8", integerPredicatesName);
        classUnit.methods().filter((method) -> method.getName().equals("even"))
                .forEach((method) -> {
                    final BoogieFunctionExtractor extractor = new BoogieFunctionExtractor();
                    extractor.convert(method);
                    FunctionDeclaration declaration = extractor.getResult();
                    StringBuilder builder = new StringBuilder();
                    declaration.print(builder);
                    System.out.println("--------------------------");
                    System.out.println(builder.toString());
                    System.out.println("--------------------------");
                });
    }

}
