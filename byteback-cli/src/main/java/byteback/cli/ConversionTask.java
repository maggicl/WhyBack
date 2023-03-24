package byteback.cli;

import byteback.analysis.Namespace;
import byteback.converter.soottoboogie.Prelude;
import byteback.converter.soottoboogie.program.ProgramConverter;
import byteback.frontend.boogie.ast.Program;
import java.util.Iterator;

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
		
		final Iterator<SootClass> classIterator = scene.getApplicationClasses().snapshotIterator();

		while (classIterator.hasNext()) {
			final SootClass clazz = classIterator.next();

			if (!Namespace.isAnnotationClass(clazz)) {
				ProgramConverter.v().convert(clazz).inject(program);
			}
		}

		program = prelude.program().merge(program);
		program.inferModifies();

		return program;
	}

}
