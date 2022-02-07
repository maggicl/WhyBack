package byteback.core.context;

import soot.Scene;
import soot.SootClass;

/**
 * Instance encapsulating the state of the Soot program analysis framework.
 * <p>
 * Note that this class is intended to be a complete wrapper for every singleton
 * managed by the Soot framework.
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
     * Initializes fields with Soot singletons.
     */
    private SootContext() {
        this.scene = Scene.v();
    }

    /**
     * Loads a new class based on the canonical name.
     *
     * @param canonicalName The canonical name of the class.
     */
    public void loadClass(String canonicalName) {
        scene.loadClass(canonicalName, SootClass.BODIES);
    }

    /**
     * Loads a new class based on the canonical name.
     *
     * @param canonicalName The canonical name of the class.
     */
    public void loadClassAndSupport(String canonicalName) {
        scene.loadClassAndSupport(canonicalName);
    }

    /**
     * Computes the total number of loaded classes.
     *
     * @return Total number of classes loaded in the context.
     */
    public int getClassesCount() {
        return scene.getClasses().size();
    }

}
