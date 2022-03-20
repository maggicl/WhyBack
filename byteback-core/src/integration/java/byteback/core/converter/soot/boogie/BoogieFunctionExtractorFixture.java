package byteback.core.converter.soot.boogie;

import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaver.Parser;
import byteback.core.ResourcesUtil;
import byteback.core.representation.soot.unit.SootClassUnit;
import byteback.core.representation.soot.unit.SootMethodUnitFixture;
import byteback.frontend.boogie.ast.Program;
import byteback.frontend.boogie.util.ParserUtil;

public class BoogieFunctionExtractorFixture extends SootMethodUnitFixture {

    private static final Logger log = LoggerFactory.getLogger(BoogieFunctionExtractorFixture.class);

    public static Stream<Entry<SootClassUnit, Program>> getRegressionEntries(final String jarName) throws IOException {
        return ResourcesUtil.getBoogiePaths(jarName).flatMap((path) -> {
            final String fileName = path.getFileName().toString();
            final String className = fileName.substring(0, fileName.lastIndexOf("."));
            final SootClassUnit classUnit = getClassUnit(jarName, className);

            try {
                final Program program = ParserUtil.parseBoogieProgram(path);
                return Stream.of(new SimpleEntry<>(classUnit, program));
            } catch (final IOException exception) {
                log.error("Error while opening Boogie file {}", path, exception);
            } catch (final Parser.Exception exception) {
                log.error("Error while parsing Boogie file {}", path, exception);
            }

            return Stream.empty();
        });
    }

}
