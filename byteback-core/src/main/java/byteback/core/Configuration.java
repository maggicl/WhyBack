package byteback.core;

import com.beust.jcommander.Parameter;
import java.util.ArrayList;
import java.util.List;

public class Configuration {

	@Parameter(names = {"-cp", "--classpath"}, description = "Classpaths to be converted")
	public List<String> classPaths = new ArrayList<>();

	@Parameter(names = {"-o", "--output"}, description = "Output path for the verification conditions")
	public String outputPath;

	@Parameter(names = {"-c", "--class"}, description = "Starting classes for the conversion")
	public List<String> startingClasses;

}
