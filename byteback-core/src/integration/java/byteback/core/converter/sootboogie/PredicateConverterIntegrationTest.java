package byteback.core.converter.sootboogie;

import org.junit.Test;

import byteback.core.identifier.MemberName;
import byteback.core.identifier.Name;
import byteback.core.representation.soot.SootClassRepresentation;
import byteback.core.representation.soot.SootClassRepresentationFixture;
import byteback.frontend.boogie.ast.Program;

public class PredicateConverterIntegrationTest extends SootClassRepresentationFixture {

    @Test
    public void test() {
        final Name integerPredicatesName = new Name("byteback.dummy.IntegerPredicates");
        final SootClassRepresentation representation = getClass("java8", integerPredicatesName);
        final Program boogieProgram = new Program();
        representation.methods().filter((method) -> method.getName().equals(new MemberName("equals")))
                .forEach((method) -> {
                    PredicateConverter converter = new PredicateConverter(boogieProgram);
                    converter.convert(method);
                });
    }

}
