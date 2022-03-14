package byteback.core;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class for accessing test resources.
 */
public class ResourcesUtil {

    /**
     * Base path to the resource folder.
     */
    final private static Path resourcesPath = Paths.get("src", "integration", "resources");

    /**
     * Base path to the compiled resource folder.
     */
    final private static Path compiledPath = resourcesPath.resolve("compiled");

    /**
     * Fetches the path to the jar of a dummy project.
     *
     * @param jarName The name of the dummy project.
     * @return The path to the dummy project.
     * @throws FileNotFoundException If the resource could not be located.
     */
    public static Path getJarPath(final String jarName) throws FileNotFoundException {
        final Path jarPath = compiledPath.resolve(jarName + "-all.jar");

        if (Files.exists(jarPath)) {
            return jarPath;
        } else {
            throw new FileNotFoundException("Could not find resource at " + jarPath.toString());
        }
    }

}
