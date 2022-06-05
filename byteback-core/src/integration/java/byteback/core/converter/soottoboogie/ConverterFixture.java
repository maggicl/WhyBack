package byteback.core.converter.soottoboogie;

import beaver.Parser;
import byteback.core.Parameter;
import byteback.core.ResourcesUtil;
import byteback.core.converter.soottoboogie.program.ProgramConverter;
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

	static final Logger log = LoggerFactory.getLogger(ConverterFixture.class);

	public static Stream<Entry<SootClass, Program>> entries(final String jarName) throws IOException {
		return ResourcesUtil.getBoogiePaths(jarName).flatMap((path) -> {
			final String fileName = path.getFileName().toString();
			final String className = fileName.substring(0, fileName.lastIndexOf("."));
			final SootClass clazz = getSootClass(jarName, className);

			try {
				final Program program = ParserUtil.parseBoogieProgram(path);
				log.info("Creating entry for {}", className);
				return Stream.of(new SimpleEntry<>(clazz, program));
			} catch (final IOException exception) {
				log.error("Error while opening Boogie file {}", path, exception);
			} catch (final Parser.Exception exception) {
				log.error("Error while parsing Boogie file {}", path, exception);
			}

			throw new RuntimeException("Could not parse " + path);
		});
	}

	public static Stream<Parameter<Program>> parameters(final String jarName) throws IOException {
		Prelude.instance().loadDefault();
		resetContext();

		return entries(jarName).flatMap((entry) -> {
			final SootClass clazz = entry.getKey();

			try {
				final Program expected = entry.getValue();
				final Program actual = ProgramConverter.instance().convert(clazz);
				Prelude.instance().inject(actual);
				actual.inferModifies();

				return Stream.of(new Parameter<>(expected, actual));
			} catch (final ConversionException exception) {
				log.error("Error while converting class {} from {}", clazz.getName(), jarName);
			}

			throw new RuntimeException("Could not convert entry");
		});
	}

}
