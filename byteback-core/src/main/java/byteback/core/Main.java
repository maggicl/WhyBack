package byteback.core;

import byteback.core.context.soot.SootContext;
import byteback.core.converter.soottoboogie.program.ContextConverter;
import com.beust.jcommander.ParameterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

	public static Logger log = LoggerFactory.getLogger(Main.class);

	public static void convert(final Configuration configuration) {
		final var converter = ContextConverter.instance();
    SootContext.instance().configure(configuration);
    log.info("Converting classes");
    System.out.println(converter.convert().print());
	}

	public static void main(final String[] args) {
		final var configuration = new Configuration();

		try {
			configuration.parse(args);

			if (configuration.getHelp()) {
				configuration.getJCommander().usage();
			} else {
        convert(configuration);
      }
    } catch (final ParameterException exception) {
			log.error("Error while parsing program arguments: {}", exception.getMessage());
			exception.getJCommander().usage();
		}
	}

}
