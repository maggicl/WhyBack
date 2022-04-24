package byteback.core;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import java.nio.file.Path;
import java.util.List;

public class Configuration {

	@Parameter(names = "--help", help = true)
	private boolean help;

	@Parameter(names = {"-cp", "--classpath"}, description = "Classpath to be converted", required = true)
	private List<Path> classPaths;

	@Parameter(names = {"-c", "--class"}, description = "Starting class for the conversion", required = true)
	private List<String> startingClasses;

	@Parameter(names = {"-p", "--prelude"}, description = "Path to the prelude file")
	private String preludePath;

	@Parameter(names = {"-o", "--output"}, description = "Path to the output verification conditions")
	private String outputPath;

	private JCommander jCommander;

	public boolean getHelp() {
		return help;
	}

	public List<Path> getClassPaths() {
		return classPaths;
	}

	public List<String> getStartingClasses() {
		return startingClasses;
	}

	public String getPreludePath() {
		return preludePath;
	}

	public String getOutputPath() {
		return outputPath;
	}

	public JCommander getJCommander() {
		return jCommander;
	}

	public void parse(final String[] args) {
		jCommander = JCommander.newBuilder().addObject(this).build();
		jCommander.parse(args);
	}

}
