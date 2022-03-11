package byteback.core.converter.soot.boogie;

import org.junit.Test;

import byteback.core.representation.unit.soot.SootClassProxy;
import byteback.core.representation.unit.soot.SootClassProxyFixture;
import byteback.frontend.boogie.ast.FunctionDeclaration;
import byteback.frontend.boogie.ast.Program;

public class BoogiePredicateExtractorIntegrationTest extends SootClassProxyFixture {

    @Test
    public void test() {
        final String integerPredicatesName = "byteback.dummy.IntegerPredicates";
        final SootClassProxy representation = getClassProxy("java8", integerPredicatesName);
        final Program program = new Program();
        representation.methods().filter((method) -> method.getName().equals("equals"))
                .forEach((method) -> {
                    BoogieFunctionConverter converter = new BoogieFunctionConverter(program);
                    FunctionDeclaration declaration = converter.convert(method);
                });
    }

}
