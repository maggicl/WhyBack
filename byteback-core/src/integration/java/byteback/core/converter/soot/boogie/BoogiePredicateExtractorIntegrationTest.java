package byteback.core.converter.soot.boogie;

import byteback.core.representation.unit.soot.SootClassUnitFixture;
import org.junit.Test;

import byteback.core.representation.unit.soot.SootClassUnit;
import byteback.frontend.boogie.ast.FunctionDeclaration;
import byteback.frontend.boogie.ast.Program;

public class BoogiePredicateExtractorIntegrationTest extends SootClassUnitFixture {

    @Test
    public void test() {
        final String integerPredicatesName = "byteback.dummy.IntegerPredicates";
        final SootClassUnit classUnit = getClassUnit("java8", integerPredicatesName);
        final Program program = new Program();
        classUnit.methods().filter((method) -> method.getName().equals("equals"))
                .forEach((method) -> {
                    final BoogieFunctionConverter converter = new BoogieFunctionConverter(program);
                    final FunctionDeclaration declaration = converter.convert(method);
                });
    }

}
