package byteback.core.context.soot;

import byteback.core.Configuration;
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
import soot.SootClass;
import soot.options.Options;

public class SootContext {

	private static final Logger log = LoggerFactory.getLogger(SootContext.class);

	private static SootContext instance = new SootContext();

	public static SootContext v() {
		return instance;
	}

	private static Scene scene() {
		return Scene.v();
	}

	private static Options options() {
		return Options.v();
	}

	private SootContext() {
		configure();
	}

	private void configure() {
		options().setPhaseOption("jb", "use-original-names:true");
		options().set_whole_program(true);
		scene().loadBasicClasses();
		log.info("Soot context initialized successfully");
	}

	public void reset() {
		G.reset();
		configure();
	}

	public void prependClassPath(final Path path) {
		final String classPath = scene().getSootClassPath();
		scene().setSootClassPath(path.toAbsolutePath() + ":" + classPath);
	}

	public List<Path> getSootClassPath() {
		final String[] parts = scene().getSootClassPath().split(":");

		return Arrays.stream(parts).map(Paths::get).collect(Collectors.toList());
	}

	public SootClass loadClass(final String name) throws ClassLoadException {
		try {
			final soot.SootClass clazz = scene().loadClass(name, soot.SootClass.BODIES);
			log.info("Loaded {} into context", name);

			return clazz;
		} catch (AssertionError exception) {
			log.error("Failed to load {}", name, exception);
			throw new ClassLoadException(name);
		}
	}

	public SootClass loadClassAndSupport(final String name) throws ClassLoadException {
		try {
			final soot.SootClass clazz = scene().loadClassAndSupport(name);
			log.info("Loaded {} and support classes into context", name);

			return clazz;
		} catch (AssertionError exception) {
			log.error("Failed to load {}", name, exception);
			throw new ClassLoadException(name);
		}
	}

	public Optional<SootClass> getSootClass(final String name) {
		final soot.SootClass clazz = scene().getSootClass(name);

		if (clazz == null) {
			return Optional.empty();
		} else {
			return Optional.of(clazz);
		}
	}

	public int getClassesCount() {
		return scene().getClasses(SootClass.BODIES).size();
	}

	public Stream<SootClass> classes() {
		return scene().getClasses(SootClass.BODIES).stream();
	}

	public void configure(final Configuration configuration) {
		for (final Path path : configuration.getClassPaths()) {
			prependClassPath(path);
		}

		for (final String className : configuration.getStartingClasses()) {
			loadClassAndSupport(className);
		}
	}

}
