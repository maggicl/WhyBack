package byteback.core.context;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import byteback.core.identifier.ClassName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import byteback.core.representation.SootClassRepresentation;
import soot.G;
import soot.Scene;
import soot.SootClass;
import soot.options.Options;

/**
 * Instance encapsulating the state of the Soot program analysis framework.
 * <p>
 * This class is intended to be a complete wrapper for every singleton managed
 * by the Soot framework.
 * <p>
 * Note that since this class wraps every singleton that is managed by Soot,
 * modifying the global soot options externally may result in an undefined
 * behavior of this context. To solve this, one possible solution could be to
 * isolate the Soot singletons by using another classloader. This would in
 * principle also allow us to keep multiple Soot contexts at the same time,
 * without being constrained to the singleton pattern.
 */
public class SootContext implements Context<SootClassRepresentation> {

    private static Logger log = LoggerFactory.getLogger(SootContext.class);

    /**
     * Singleton value.
     */
    private static final SootContext instance = new SootContext();

    /**
     * Singleton accessor.
     *
     * @return Singleton instance.
     */
    public static SootContext instance() {
        return instance;
    }

    /**
     * Yields the soot.Scene singleton.
     *
     * @return Soot's {@code Scene} singleton.
     */
    private static Scene scene() {
        return Scene.v();
    }

    /**
     * Yields the soot.Options singleton.
     *
     * @return Soot's {@code Options} singleton.
     */
    private static Options options() {
        return Options.v();
    }

    /**
     * Initializes fields with the singletons and sets the global Soot options.
     */
    private SootContext() {
        configure();
    }

    /**
     * Configures Soot's parameters.
     */
    private void configure() {
        options().set_output_format(Options.output_format_jimple);
        options().set_whole_program(true);
        scene().loadBasicClasses();
        log.info("Soot context initialized successfully");
    }

    /**
     * Resets Soot's globals.
     */
    public void reset() {
        G.reset();
        configure();
    }

    /**
     * Prepends the classpath of the Soot scene.
     *
     * @param path Path to be prepended to the classpath.
     */
    public void prependClassPath(final Path path) {
        final String classPath = scene().getSootClassPath();
        scene().setSootClassPath(path.toAbsolutePath().toString() + ":" + classPath);
    }

    /**
     * Returns the classpath of the Soot scene.
     *
     * @return Current classpath loaded in the scene.
     */
    public List<Path> getClassPath() {
        final String[] parts = scene().getSootClassPath().split(":");

        return Arrays.stream(parts).map((part) -> Paths.get(part)).collect(Collectors.toList());
    }

    /**
     * Loads a class in the Soot scene.
     *
     * @param className Qualified name of a class present in the classpath.
     * @throws ClassLoadException If the class could not be loaded into the context.
     */
    @Override
    public void loadClass(final ClassName className) throws ClassLoadException {
        try {
            scene().loadClass(className.toString(), SootClass.BODIES);
            log.info("Loaded {} in context", className);
        } catch (AssertionError exception) {
            log.error("Failed to load {}", className);
            throw new ClassLoadException(this, className);
        }
    }

    /**
     * Loads a root class and its supporting classes in the Soot scene.
     *
     * @see #loadClass(ClassName)
     */
    @Override
    public void loadClassAndSupport(final ClassName className) throws ClassLoadException {
        try {
            scene().loadClassAndSupport(className.toString());
            log.info("Loaded {} and support classes in context", className);
        } catch (AssertionError exception) {
            log.error("Failed to load {}", className);
            throw new ClassLoadException(this, className);
        }
    }

    /**
     * Computes the number of classes loaded in the Soot scene.
     *
     * @return Total number of classes in the Soot scene.
     */
    @Override
    public int getClassesCount() {
        return scene().getClasses().size();
    }

    /**
     * Computes the soot class representation for every soot class loaded in the
     * Scene.
     *
     * @return The stream of loaded classes in the current context.
     */
    @Override
    public Stream<SootClassRepresentation> classes() {
        return scene().getClasses().stream().map(SootClassRepresentation::new);
    }

}
