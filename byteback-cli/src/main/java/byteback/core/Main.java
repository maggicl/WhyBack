package byteback.core;

import byteback.core.context.soot.SootContext;
import byteback.core.converter.soottoboogie.Prelude;
import byteback.core.converter.soottoboogie.program.ContextConverter;
import byteback.frontend.boogie.ast.Program;
import com.beust.jcommander.ParameterException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

	public static Logger log = LoggerFactory.getLogger(Main.class);

	public static void convert(final Configuration configuration) {
		final Program program = ContextConverter.instance().convert();
		final PrintStream output;

		if (configuration.getOutputPath() != null) {
			final File file = configuration.getOutputPath().toFile();

			try {
				file.createNewFile();
				output = new PrintStream(new FileOutputStream(file));
			} catch (final IOException exception) {
				log.error("Cannot output program to file {}", file.getPath());
				throw new RuntimeException("Unable to produce output");
			}
		} else {
			output = System.out;
		}

		output.print(program.print());
		output.close();
	}

	public static void initialize(final Configuration configuration) {
		log.info("Configuring contexts");
		SootContext.v().configure(configuration);
		Prelude.v().configure(configuration);
	}

	public static void main(final String[] args) {
		final var configuration = new Configuration();
		final long totalStart = System.currentTimeMillis();

		try {
			configuration.parse(args);

			if (configuration.getHelp()) {
				configuration.getJCommander().usage();
			} else {
				initialize(configuration);
				final long conversionStart = System.currentTimeMillis();
				log.info("Converting classes");
				convert(configuration);
				final long endTime = System.currentTimeMillis();
				final long totalTime = endTime - totalStart;
				final long conversionTime = endTime - conversionStart;
				log.info("Conversion completed in {}ms, total time {}ms", conversionTime, totalTime);
			}
		} catch (final ParameterException exception) {
			log.error("Error while parsing program arguments: {}", exception.getMessage());
			exception.getJCommander().usage();
		}
	}

}
