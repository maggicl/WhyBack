package byteback.core.converter.soot.boogie;

import org.junit.Test;

import byteback.core.representation.unit.soot.SootClassProxy;
import byteback.core.representation.unit.soot.SootClassProxyFixture;
import byteback.frontend.boogie.ast.Program;

public class PredicateConverterIntegrationTest extends SootClassProxyFixture {

    @Test
    public void test() {
        final String integerPredicatesName = "byteback.dummy.IntegerPredicates";
        final SootClassProxy representation = getClassProxy("java8", integerPredicatesName);
        final Program boogieProgram = new Program();
        representation.methods().filter((method) -> method.getName().equals("equals"))
                .forEach((method) -> {
                    PredicateConverter converter = new PredicateConverter(boogieProgram);
                    converter.convert(method);
                });
    }

}
