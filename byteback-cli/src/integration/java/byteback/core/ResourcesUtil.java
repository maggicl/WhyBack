package byteback.core;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Utility class for accessing test resources.
 */
public class ResourcesUtil {

	/**
	 * Base path to the resource folder.
	 */
	private final static Path resourcesPath = Paths.get("src", "integration", "resources");

	/**
	 * Base path to the compiled resource directory.
	 */
	private final static Path compiledPath = resourcesPath.resolve("compiled");

	/**
	 * Base path to the expected files generated from the compiled classes.
	 */
	private final static Path regressionPath = resourcesPath.resolve("regression");

	/**
	 * Fetches the path to the jar of a dummy project.
	 *
	 * @param jarName
	 *            The name of the dummy project.
	 * @return The path to the dummy project.
	 * @throws FileNotFoundException
	 *             If the resource could not be located.
	 */
	public static Path getJarPath(final String jarName) throws FileNotFoundException {
		final Path jarPath = compiledPath.resolve(jarName + "-all.jar");

		if (Files.exists(jarPath)) {
			return jarPath;
		} else {
			throw new FileNotFoundException("Could not find resource at " + jarPath.toString());
		}
	}

	public static Stream<Path> getBoogiePaths(final String jarName) throws IOException {
		final Path regressionRoot = regressionPath.resolve(jarName).resolve("boogie");

		return Files.list(regressionRoot);
	}

}
