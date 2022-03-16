package byteback.core.context.soot;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import byteback.core.context.ClassLoadException;
import byteback.core.context.Context;
import byteback.core.representation.unit.soot.SootClassUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import soot.G;
import soot.Scene;
import soot.SootClass;
import soot.options.Options;

/**
 * Instance encapsulating the state of the Soot program analysis framework.
 *
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
public class SootContext implements Context<SootClassUnit> {

    private static final Logger log = LoggerFactory.getLogger(SootContext.class);

    /**
     * Singleton instance.
     */
    private static final SootContext instance = new SootContext();

    /**
     * Accessor for the singleton instance.
     *
     * @return Singleton context instance.
     */
    public static SootContext instance() {
        return instance;
    }

    /**
     * Yields the soot.Scene singleton.
     *
     * @return Soot's {@link Scene} singleton.
     */
    private static Scene scene() {
        return Scene.v();
    }

    /**
     * Yields the soot.Options singleton.
     *
     * @return Soot's {@link Options} singleton.
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
     * Setter that prepends the classpath of the {@link Scene}.
     *
     * @param path Path to be prepended to the classpath.
     */
    public void prependClassPath(final Path path) {
        final String classPath = scene().getSootClassPath();
        scene().setSootClassPath(path.toAbsolutePath().toString() + ":" + classPath);
    }

    /**
     * Getter for the classpath loaded in the {@link Scene}.
     *
     * @return Current classpath loaded in the scene.
     */
    public List<Path> getClassPath() {
        final String[] parts = scene().getSootClassPath().split(":");

        return Arrays.stream(parts).map(Paths::get).collect(Collectors.toList());
    }

    /**
     * Loads a class in the {@link Scene}.
     *
     * @param className Qualified name of a class present in the Soot's classpath.
     * @return The Soot intermediate representation of the loaded class.
     */
    @Override
    public SootClassUnit loadClass(final String className) throws ClassLoadException {
        try {
            final SootClass sootClass = scene().loadClass(className.toString(), SootClass.BODIES);
            log.info("Loaded {} in context", className);

            return new SootClassUnit(sootClass);
        } catch (AssertionError exception) {
            log.error("Failed to load {}", className, exception);
            throw new ClassLoadException(this, className);
        }
    }

    /**
     * Loads a root class and its supporting classes in the {@link Scene}.
     *
     * @param className Qualified name of the root class present in the Soot's
     *                  classpath.
     * @return The Soot intermediate representation of the loaded root class.
     */
    @Override
    public SootClassUnit loadClassAndSupport(final String className) throws ClassLoadException {
        try {
            final SootClass sootClass = scene().loadClassAndSupport(className);
            log.info("Loaded {} and support classes in context", className);

            return new SootClassUnit(sootClass);
        } catch (AssertionError exception) {
            log.error("Failed to load {}", className, exception);
            throw new ClassLoadException(this, className);
        }
    }

    /**
     * Returns a class unit that was already loaded into the context
     *
     * @param className Qualified name of the class.
     * @return The class corresponding to the given {@code className}.
     */
    public Optional<SootClassUnit> getClass(final String className) {
        final SootClass sootClass = scene().getSootClass(className);

        if (sootClass == null) {
            return Optional.empty();
        } else {
            return Optional.of(new SootClassUnit(sootClass));
        }
    }

    /**
     * Getter for the number of classes loaded in the {@link Scene}.
     *
     * @return Total number of classes in the Soot scene.
     */
    @Override
    public int getClassesCount() {
        return scene().getClasses().size();
    }

    /**
     * Getter for the stream of Soot representations loaded in the context.
     *
     * @return The stream of loaded classes in the current context.
     */
    @Override
    public Stream<SootClassUnit> classes() {
        return scene().getClasses().stream().map(SootClassUnit::new);
    }

    /**
     * Getter for the predefined identifier of this context.
     *
     * @return The {@link SootContext} identifier.
     */
    @Override
    public String getName() {
        return "SOOT_CONTEXT";
    }

}
