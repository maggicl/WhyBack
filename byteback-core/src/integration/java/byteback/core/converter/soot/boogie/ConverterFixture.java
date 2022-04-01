package byteback.core.converter.soot.boogie;

import beaver.Parser;
import byteback.core.ResourcesUtil;
import byteback.core.representation.soot.unit.SootClass;
import byteback.core.representation.soot.unit.SootClassFixture;
import byteback.frontend.boogie.ast.Program;
import byteback.frontend.boogie.util.ParserUtil;
import java.io.IOException;
import java.util.AbstractMap.SimpleEntry;
import java.util.Map.Entry;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConverterFixture extends SootClassFixture {

	private static final Logger log = LoggerFactory.getLogger(ConverterFixture.class);

	public static Stream<Entry<SootClass, Program>> getRegressionEntries(final String jarName) throws IOException {
		return ResourcesUtil.getBoogiePaths(jarName).flatMap((path) -> {
			final String fileName = path.getFileName().toString();
			final String className = fileName.substring(0, fileName.lastIndexOf("."));
			final SootClass clazz = getSootClass(jarName, className);

			try {
				final Program program = ParserUtil.parseBoogieProgram(path);
				return Stream.of(new SimpleEntry<>(clazz, program));
			} catch (final IOException exception) {
				log.error("Error while opening Boogie file {}", path, exception);
			} catch (final Parser.Exception exception) {
				log.error("Error while parsing Boogie file {}", path, exception);
			}

			return Stream.empty();
		});
	}

}
