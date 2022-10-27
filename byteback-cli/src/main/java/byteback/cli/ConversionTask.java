package byteback.cli;

import byteback.analysis.util.SootClasses;
import byteback.converter.soottoboogie.program.ProgramConverter;
import byteback.frontend.boogie.ast.Program;
import soot.Scene;
import soot.SootClass;

public class ConversionTask implements Runnable {

	private final Scene scene;

	private Program prelude;

	public ConversionTask(final Scene scene, final Program program) {
		this.scene = scene;
		this.prelude = program;
	}

	public void run() {
		final Program program = new Program();

		for (final SootClass clazz : scene.getClasses()) {
			if (clazz.resolvingLevel() >= SootClass.SIGNATURES
					&& !SootClasses.isBasicClass(clazz)) {
				program.inject(ProgramConverter.v().convert(clazz));
			}
		}

		prelude.inject(program);
	}

	public Program getProgram() {
		return prelude;
	}

}
