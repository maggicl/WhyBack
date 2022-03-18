package byteback.core.converter.soot.boogie;

import static org.junit.Assert.assertEquals;

import java.util.stream.Stream;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import byteback.core.representation.unit.soot.SootMethodUnit;
import byteback.frontend.boogie.ast.FunctionDeclaration;
import byteback.frontend.boogie.ast.PrintUtil;
import byteback.frontend.boogie.ast.Program;

@RunWith(Parameterized.class)
public class BoogieFunctionExtractorIntegrationTest extends BoogieFunctionExtractorFixture {

    @Parameters
    public static Iterable<RegressionEntry<FunctionDeclaration>> getFunctionEntries() {
        final Stream<Program> programs = getExpectedBoogiePrograms("java8");

        return programs.flatMap((program) -> program.functions().stream().flatMap((function) -> {
            try {
                final FunctionDeclaration declaration = function.declaration();
                final String javaName = toJavaMethodName(declaration.getDeclarator().getName());
                final MethodIdentifier javaIdentifier = javaMethodIdentifier(javaName);
                final SootMethodUnit methodUnit = getMethodUnit("java8", javaIdentifier.className,
                        javaIdentifier.methodName);
                final RegressionEntry<FunctionDeclaration> entry = new RegressionEntry<>(declaration,
                        new BoogieFunctionExtractor(methodUnit).convert());

                return Stream.of(entry);
            } catch (final RuntimeException exception) {
                return Stream.empty();
            }
        }))::iterator;
    }

    private final RegressionEntry<FunctionDeclaration> entry;

    public BoogieFunctionExtractorIntegrationTest(final RegressionEntry<FunctionDeclaration> entry) {
        this.entry = entry;
    }

    @Test
    public void test() {
        assertEquals(PrintUtil.toString(entry.actual), PrintUtil.toString(entry.expected));
    }

}
