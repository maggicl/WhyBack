package byteback.core.context;

import soot.Scene;

/**
 * Instance encapsulating the state of the Soot program analysis framework.
 * <p>
 * Note that this class is intended to be a complete wrapper for every singleton
 * managed by the Soot framework.
 */
public class SootContext {

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

}
