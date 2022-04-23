package byteback.core;

import com.beust.jcommander.ParameterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

	public static Logger log = LoggerFactory.getLogger(Main.class);

	public static void main(final String[] args) {
		final Configuration configuration = new Configuration();

		try {
			configuration.parse(args);

			if (configuration.getHelp()) {
				configuration.getJCommander().usage();
			} else {
				System.out.println("Executing...");
			}
		} catch (final ParameterException exception) {
			log.error("Error while parsing program arguments: {}", exception.getMessage());
			exception.getJCommander().usage();
		}
	}

}
