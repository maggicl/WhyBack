package byteback.core;

import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Utility class for accessing test resources.
 */
public class ResourcesUtil {

    final private static Path resourcesPath = Paths.get("src", "integration", "resources");

    final private static Path compiledPath = resourcesPath.resolve("compiled");

    /**
     * Fetches the path to the jar of a dummy project.
     *
     * @param resourceName The name of the dummy project.
     * @return The path to the dummy project.
     * @throws FileNotFoundException If the resource could not be located.
     */
    public static Path getJarPath(String resourceName) throws FileNotFoundException {
        final Path jarPath = compiledPath.resolve(Paths.get(resourceName, resourceName + ".jar"));

        if (Files.exists(jarPath)) {
            return jarPath;
        } else {
            throw new FileNotFoundException("Could not find resource at " + jarPath.toString());
        }
    }

}
