package byteback.core.converter.soot.boogie;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import byteback.core.ResourcesUtil;
import byteback.core.representation.unit.soot.SootMethodUnit;
import byteback.core.representation.unit.soot.SootMethodUnitFixture;
import byteback.frontend.boogie.ast.FunctionDeclaration;
import byteback.frontend.boogie.ast.Program;
import byteback.frontend.boogie.util.ParserUtil;

public class BoogieFunctionExtractorFixture extends SootMethodUnitFixture {

    private final Logger log = LoggerFactory.getLogger(BoogieFunctionExtractorFixture.class);

    public static class MethodIdentifier {

        public final String className;

        public final String methodName;

        public MethodIdentifier(final String className, final String methodName) {
            this.className = className;
            this.methodName = methodName;
        }

    }

    public static class RegressionEntry<T> {

        public final T actual;

        public final T expected;

        public RegressionEntry(final T actual, final T expected) {
            this.actual = actual;
            this.expected = expected;
        }

    }

    public static String toJavaMethodName(final String boogieMethodName) {
        final char[] nameArray = boogieMethodName.toCharArray();
        final int start = boogieMethodName.indexOf("#");
        final int end = boogieMethodName.lastIndexOf("#");
        nameArray[start] = '(';
        nameArray[end] = ')';

        return new String(nameArray).replace("#", ",");
    }

    public static MethodIdentifier javaMethodIdentifier(final String javaMethodName) {
        final String[] fullParts = javaMethodName.split("\\(");
        final String[] nameParts = fullParts[0].split("\\.");
        final String className = String.join(".", Arrays.copyOfRange(nameParts, 0, nameParts.length - 1));
        final String methodIdentifier = String.join("(", nameParts[nameParts.length - 1], fullParts[1]);

        return new MethodIdentifier(className, methodIdentifier);
    }

    public Stream<Program> getExpectedBoogiePrograms(final String jarName) {
        try {
            final Stream<Path> paths = ResourcesUtil.getBoogiePaths(jarName);

            return paths.flatMap((path) -> {
                try {
                    return Stream.of(ParserUtil.parseBoogieProgram(path));
                } catch (final Exception exception) {
                    log.error("Could not parse the program at {}", path, exception);

                    return Stream.empty();
                }
            });
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

    public Stream<RegressionEntry<FunctionDeclaration>> getFunctionEntries() {
        final Stream<Program> programs = getExpectedBoogiePrograms("java8");

        return programs.flatMap((program) -> {
            return program.functions().stream().flatMap((function) -> {
                try {
                    final FunctionDeclaration declaration = function.declaration();
                    final String javaName = toJavaMethodName(declaration.getDeclarator().getName());
                    final MethodIdentifier javaIdentifier = javaMethodIdentifier(javaName);
                    final SootMethodUnit methodUnit = getMethodUnit("java8", javaIdentifier.className,
                            javaIdentifier.methodName);
                    final RegressionEntry<FunctionDeclaration> entry = new RegressionEntry<>(declaration,
                            new BoogieFunctionExtractor(methodUnit).convert());

                    return Stream.of(entry);
                } catch (RuntimeException exception) {
                    log.error("Error while loading function", exception);

                    return Stream.empty();
                }
            });
        });
    }

}
