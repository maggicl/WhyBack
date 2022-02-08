package byteback.core.context;

import java.nio.file.Path;

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
     * SootContext singleton value.
     */
    private static final SootContext instance = new SootContext();

    /**
     * Singleton accessor.
     *
     * @return The <code>SootContext</code> singleton instance.
     */
    public static SootContext instance() {
        return instance;
    }

    /**
     * The main scene managed by Soot.
     */
    private final Scene scene;

    /**
     * The options for the scene.
     */
    private final Options options;

    /**
     * Configures Soot parameters.
     */
    private void configureSoot() {
        options.set_output_format(Options.output_format_jimple);
        options.set_whole_program(true);
        scene.loadBasicClasses();
    }

    /**
     * Initializes fields with the singletons and sets the global Soot options.
     */
    private SootContext() {
        this.scene = Scene.v();
        this.options = Options.v();
        configureSoot();
    }

    /**
     * Prepends the classpath of the Soot scene.
     *
     * @param path The path to be prepended to the classpath.
     */
    public void prependClassPath(final Path path) {
        final String oldPath = scene.getSootClassPath();
        scene.setSootClassPath(path.toString() + ":" + oldPath);
    }

    /**
     * Loads a class in the Soot scene.
     *
     * @param canonicalName A name of a class present in the Soot classpath.
     */
    @Override
    public void loadClass(final String canonicalName) {
        scene.loadClass(canonicalName, SootClass.BODIES);
    }

    /**
     * Computes the number of classes loaded in the Soot scene.
     *
     * @return The total number of classes in the Soot scene.
     */
    @Override
    public int getClassesCount() {
        return scene.getClasses().size();
    }

}
