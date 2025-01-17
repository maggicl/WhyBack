package byteback.cli;

import byteback.analysis.ClassInjector;
import byteback.analysis.RootResolver;
import byteback.converter.soottoboogie.Prelude;
import byteback.whyml.syntax.HeapKind;
import com.beust.jcommander.ParameterException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Scene;
import soot.SootClass;
import soot.options.Options;
import soot.util.Chain;
import soot.util.HashChain;

public class Main {

	public static final Logger log = LoggerFactory.getLogger(Main.class);

	public static final Scene scene = Scene.v();

	public static final Options options = Options.v();

	public static final Prelude prelude = Prelude.v();

	public static final RootResolver resolver = RootResolver.v();

	public static final ClassInjector injector = ClassInjector.v();

	public static void convert(final byteback.cli.Configuration configuration) {
		final PrintStream output;

		if (configuration.getOutputPath() != null) {
			final File file = configuration.getOutputPath().toFile();

			try {
				file.createNewFile();
				output = new PrintStream(new FileOutputStream(file));
			} catch (final IOException exception) {
				log.error("Cannot output program to file {}", file.getPath());
				throw new RuntimeException("Unable to produce output");
			}
		} else {
			output = System.out;
		}

		final ConversionTask task;
		if (configuration.useWhy()) {
			final String heapKind = configuration.getHeapKind().get(0);
			final HeapKind kind;
			if ("machine".equalsIgnoreCase(heapKind)) {
				kind = HeapKind.MACHINE;
			} else if ("math".equalsIgnoreCase(heapKind)) {
				kind = HeapKind.MATH;
			} else {
				throw new IllegalArgumentException("given heap kind must be either 'machine' or 'math', given '" + heapKind + "'");
			}
			task = new WhyConversionTask(resolver, kind);
		} else {
			task = new BoogieConversionTask(resolver, prelude);
		}
		output.print(task.run().print());
		output.close();
	}

	public static void initialize(final byteback.cli.Configuration configuration) {
		final List<Path> classPaths = configuration.getClassPaths();
		final List<String> startingClassNames = configuration.getStartingClasses();
		final Path preludePath = configuration.getPreludePath();

		options.set_allow_phantom_refs(true);
		options.set_keep_line_number(true);
		options.set_keep_offset(true);
		scene.allowsPhantomRefs();

		options.allow_phantom_refs();
		options.setPhaseOption("jb", "use-original-names:true");
		options.setPhaseOption("gb.a1", "enabled:false");
		options.setPhaseOption("gb.cf", "enabled:false");
		options.setPhaseOption("gb.a2", "enabled:false");
		options.setPhaseOption("gb.ule", "enabled:false");

		for (final Path classPath : classPaths) {
			scene.setSootClassPath(scene.getSootClassPath() + File.pathSeparator + classPath);
		}

		scene.loadBasicClasses();

		final Chain<SootClass> startingClasses = new HashChain<>();
		startingClasses.add(scene.loadClassAndSupport("byteback.annotations.KotlinIntrinsicsSpec"));
		startingClasses.add(scene.loadClassAndSupport("byteback.annotations.ObjectSpec"));
		startingClasses.add(scene.loadClassAndSupport("byteback.annotations.ExceptionSpec"));
		startingClasses.add(scene.loadClassAndSupport("byteback.annotations.CollectionSpec"));
		startingClasses.add(scene.loadClassAndSupport("byteback.annotations.InvokeDynamicSpec"));
		startingClasses.add(scene.loadClassAndSupport("byteback.annotations.IndexOutOfBoundsExceptionSpec"));
		startingClasses.add(scene.loadClassAndSupport("byteback.annotations.ConcurrentModificationExceptionSpec"));
		startingClasses.add(scene.loadClassAndSupport("byteback.annotations.NoSuchElementExceptionSpec"));
		startingClasses.add(scene.loadClassAndSupport("byteback.annotations.IllegalStateExceptionSpec"));
		startingClasses.add(scene.loadClassAndSupport("byteback.annotations.IllegalArgumentExceptionSpec"));
		startingClasses.add(scene.loadClassAndSupport("byteback.annotations.NullPointerExceptionSpec"));

		for (final String startingClassName : startingClassNames) {
			final SootClass startingClass = scene.loadClassAndSupport(startingClassName);
			startingClasses.add(startingClass);
		}

		injector.inject(scene.getClasses());
		resolver.setCheckNullDereference(Configuration.v().getTransformNullCheck());
		resolver.setCheckArrayDereference(Configuration.v().getTransformArrayCheck());
		resolver.setMakeThrowStmtReturn(!configuration.useWhy());
		resolver.setPreserveInvariants(configuration.useWhy());
		resolver.setTransformSwitchToIf(configuration.useWhy());
		resolver.resolve(startingClasses);

		if (!configuration.useWhy()) {
			if (preludePath != null) {
				prelude.loadFile(preludePath);
			} else {
				prelude.loadDefault();
			}
		}
	}

	public static void main(final String[] args) {
		final byteback.cli.Configuration config = byteback.cli.Configuration.v();
		final long totalStart = System.currentTimeMillis();

		try {
			config.parse(args);

			if (config.getHelp()) {
				config.getJCommander().usage();
			} else {
				initialize(config);
				final long conversionStart = System.currentTimeMillis();
				log.info("Converting classes");
				convert(config);
				final long endTime = System.currentTimeMillis();
				final long totalTime = endTime - totalStart;
				final long conversionTime = endTime - conversionStart;
				log.info("Conversion completed in {}ms, total time {}ms", conversionTime, totalTime);
			}
		} catch (final ParameterException exception) {
			log.error("Error while parsing program arguments: {}", exception.getMessage());
			exception.getJCommander().usage();
		}
	}

}
