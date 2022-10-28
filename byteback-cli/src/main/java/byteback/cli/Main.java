package byteback.cli;

import byteback.converter.soottoboogie.Prelude;

import com.beust.jcommander.ParameterException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Scene;
import soot.options.Options;

public class Main {

	public static final Logger log = LoggerFactory.getLogger(Main.class);

	public static final Scene scene = Scene.v();

	public static final Options options = Options.v();

	public static final Prelude prelude = Prelude.v();

	public static void convert(final Configuration configuration) {
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

		// Write to output
		final var task = new ConversionTask(scene, prelude);
		
		output.print(task.run().print());

		output.close();
	}

	public static void initialize(final Configuration configuration) {
		final List<Path> classPaths = configuration.getClassPaths();
		final List<String> startingClasses = configuration.getStartingClasses();
		final Path preludePath = configuration.getPreludePath();

		for (Path classPath : classPaths) {
			scene.setSootClassPath(scene.getSootClassPath() + File.pathSeparator + classPath);
		}

		options.setPhaseOption("jb", "use-original-names:true");
		scene.loadBasicClasses();

		for (String startingClass : startingClasses) {
			scene.loadClassAndSupport(startingClass);
		}

		if (preludePath != null) {
			prelude.loadFile(preludePath);
		} else {
			prelude.loadDefault();
		}
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
