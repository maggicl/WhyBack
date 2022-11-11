package byteback.cli;

import byteback.analysis.Namespace;
import byteback.analysis.util.SootClasses;
import byteback.converter.soottoboogie.Prelude;
import byteback.converter.soottoboogie.program.ProgramConverter;
import byteback.frontend.boogie.ast.Program;
import soot.Scene;
import soot.SootClass;

public class ConversionTask {

	private final Scene scene;

	private Prelude prelude;

	public ConversionTask(final Scene scene, final Prelude prelude) {
		this.scene = scene;
		this.prelude = prelude;
	}

	public Program run() {
		Program program = new Program();

		for (final SootClass clazz : scene.getClasses()) {
			if (clazz.resolvingLevel() >= SootClass.SIGNATURES
					&& !SootClasses.isBasicClass(clazz)
					&& !Namespace.isAnnotationClass(clazz)) {
				ProgramConverter.v().convert(clazz).inject(program);
			}
		}

		program = prelude.program().merge(program);
		program.inferModifies();

		return program;
	}

}
