package byteback.core.context.soot;

import byteback.core.Configuration;
import byteback.core.representation.soot.unit.SootClass;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.G;
import soot.Scene;
import soot.options.Options;

/**
 * Instance encapsulating the state of the Soot program analysis framework.
 *
 * This class is intended to be a complete wrapper for every singleton managed
 * by the Soot framework.
 * <p>
 * Note that since this class wraps every singleton that is managed by Soot,
 * modifying the global soot options externally may result in an undefined
 * behavior of this context.
 *
 * @author paganma
 */
public class SootContext {

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
		options().setPhaseOption("jb", "use-original-names:true");
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
	 * @param path
	 *            Path to be prepended to the classpath.
	 */
	public void prependClassPath(final Path path) {
		final String classPath = scene().getSootClassPath();
		scene().setSootClassPath(path.toAbsolutePath() + ":" + classPath);
	}

	/**
	 * Getter for the classpath loaded in the {@link Scene}.
	 *
	 * @return Current classpath loaded in the scene.
	 */
	public List<Path> getSootClassPath() {
		final String[] parts = scene().getSootClassPath().split(":");

		return Arrays.stream(parts).map(Paths::get).collect(Collectors.toList());
	}

	/**
	 * Loads a class in the {@link Scene}.
	 *
	 * @param name
	 *            Qualified name of a class present in the Soot's classpath.
	 * @return The Soot intermediate representation of the loaded class.
	 */
	public SootClass loadClass(final String name) throws ClassLoadException {
		try {
			final soot.SootClass sootClass = scene().loadClass(name, soot.SootClass.BODIES);
			log.info("Loaded {} into context", name);

			return new SootClass(sootClass);
		} catch (AssertionError exception) {
			log.error("Failed to load {}", name, exception);
			throw new ClassLoadException(name);
		}
	}

	/**
	 * Loads a root class and its supporting classes in the {@link Scene}.
	 *
	 * @param name
	 *            Qualified name of the root class present in the Soot's classpath.
	 * @return The Soot intermediate representation of the loaded root class.
	 */
	public SootClass loadClassAndSupport(final String name) throws ClassLoadException {
		try {
			final soot.SootClass sootClass = scene().loadClassAndSupport(name);
			log.info("Loaded {} and support classes into context", name);

			return new SootClass(sootClass);
		} catch (AssertionError exception) {
			log.error("Failed to load {}", name, exception);
			throw new ClassLoadException(name);
		}
	}

	/**
	 * Returns a class unit that was already loaded into the context
	 *
	 * @param name
	 *            Qualified name of the class.
	 * @return The class corresponding to the given {@code name}.
	 */
	public Optional<SootClass> getSootClass(final String name) {
		final soot.SootClass sootClass = scene().getSootClass(name);

		if (sootClass == null) {
			return Optional.empty();
		} else {
			return Optional.of(new SootClass(sootClass));
		}
	}

	/**
	 * Getter for the number of classes loaded in the {@link Scene}.
	 *
	 * @return Total number of classes in the Soot scene.
	 */
	public int getClassesCount() {
		return scene().getClasses().size();
	}

	/**
	 * Getter for the stream of Soot representations loaded in the context.
	 *
	 * @return The stream of loaded classes in the current context.
	 */
	public Stream<SootClass> classes() {
		return scene().getClasses().stream().map(SootClass::new);
	}

	/**
	 * Configures the context for the main conversion job.
	 *
	 * @param configuration
	 *            The {@link Configuration} instance including the {@code --class}
	 *            and {@code --classpath} options specified by the user.
	 */
	public void configure(final Configuration configuration) {
		for (final Path path : configuration.getClassPaths()) {
			prependClassPath(path);
		}

		for (final String className : configuration.getStartingClasses()) {
			loadClassAndSupport(className);
		}
	}

}
