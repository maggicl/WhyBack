package byteback.core;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import beaver.Parser;
import byteback.frontend.boogie.ast.Program;
import byteback.frontend.boogie.parser.BoogieParser;
import byteback.frontend.boogie.scanner.BoogieLexer;

/**
 * Utility class for accessing test resources.
 */
public class ResourcesUtil {

    /**
     * Base path to the resource folder.
     */
    private final static Path resourcesPath = Paths.get("src", "integration", "resources");

    /**
     * Base path to the compiled resource folder.
     */
    private final static Path compiledPath = resourcesPath.resolve("compiled");

    private final static Path regressionPath = resourcesPath.resolve("regression");

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

    public static List<Path> getRegressionPaths(final String jarName, final String type) throws IOException {
        final Path regressionRoot = regressionPath.resolve(jarName).resolve(type);

        return Files.walk(regressionRoot).collect(Collectors.toList());
    }

    public static Program parseBoogieProgram(final Path path) throws IOException, Parser.Exception {
        final Reader reader = new FileReader(path.toFile());
        final BoogieLexer lexer = new BoogieLexer(reader);
        final BoogieParser parser = new BoogieParser();
        final Program program = (Program) parser.parse(lexer);

        return program;
    }

}
