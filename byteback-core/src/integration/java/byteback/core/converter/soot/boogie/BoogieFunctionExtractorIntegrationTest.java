package byteback.core.converter.soot.boogie;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import byteback.core.RegressionParameter;
import byteback.core.ResourcesUtil;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import byteback.core.representation.soot.annotation.SootAnnotation;
import byteback.core.representation.soot.unit.SootClassUnit;
import byteback.frontend.boogie.ast.FunctionDeclaration;
import byteback.frontend.boogie.ast.PrintUtil;
import byteback.frontend.boogie.ast.Program;

@RunWith(Parameterized.class)
public class BoogieFunctionExtractorIntegrationTest extends BoogieFunctionExtractorFixture {

    private static final Logger log = LoggerFactory.getLogger(BoogieFunctionExtractorIntegrationTest.class);

    @Parameters
    public static Iterable<RegressionParameter<FunctionDeclaration>> getFunctionEntries() throws IOException {
        final String targetJar = "java8";
        final Program program = getExpectedBoogieProgram(targetJar);

        return ResourcesUtil.getBoogiePaths(targetJar).flatMap((path) -> {
            final String fileName = path.getFileName().toString();
            final String className = fileName.substring(0, fileName.lastIndexOf("."));
            final SootClassUnit classUnit = getClassUnit(targetJar, className);

            return classUnit.methods().flatMap((methodUnit) -> {
                try {
                    final Optional<SootAnnotation> pureAnnotation = methodUnit
                            .getAnnotation("Lbyteback/annotations/Contract$Pure;");

                    System.out.println(classUnit.getName());

                    if (pureAnnotation.isPresent()) {
                        final String boogieName = BoogieNameConverter.methodName(methodUnit);
                        final FunctionDeclaration expected = program.lookupFunction(boogieName)
                                .orElseThrow(() -> new IllegalAccessError(
                                        "Could not find Boogie definition for method " + methodUnit.getName()))
                                .getFunctionDeclaration();
                        final FunctionDeclaration actual = new BoogieFunctionExtractor(methodUnit).convert();

                        return Stream.of(new RegressionParameter<>(expected, actual));
                    }
                } catch (final IllegalAccessError exception) {
                    log.error("Skipping method {} of class {}", methodUnit.getName(), classUnit.getName(), exception);
                }

                return Stream.empty();
            });
        })::iterator;
    }

    private final RegressionParameter<FunctionDeclaration> entry;

    public BoogieFunctionExtractorIntegrationTest(final RegressionParameter<FunctionDeclaration> entry) {
        this.entry = entry;
    }

    @Test
    public void test() {
        assertEquals(PrintUtil.toString(entry.actual), PrintUtil.toString(entry.expected));
    }

}
