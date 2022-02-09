package byteback.core.context;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
public class SootContext implements Context {

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
     * Simple function yielding the soot.Scene singleton.
     *
     * @return Soot's {@code Scene} singleton.
     */
    private static Scene scene() {
        return Scene.v();
    }

    /**
     * Simple function yielding the soot.Options singleton.
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
        scene().setSootClassPath(path.toString() + ":" + classPath);
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
     * @param qualifiedName Qualified name of a class present in the classpath.
     */
    @Override
    public void loadClass(final QualifiedName qualifiedName) {
        scene().loadClass(qualifiedName.toString(), SootClass.BODIES);
    }

    /**
     * Loads a root class and its supporting classes in the Soot scene.
     *
     * @param qualifiedName {@see #loadClass(QualifiedName)}
     */
    @Override
    public void loadClassAndSupport(final QualifiedName qualifiedName) {
        scene().loadClassAndSupport(qualifiedName.toString());
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

}
