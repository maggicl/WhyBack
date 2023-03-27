package byteback.cli;

import byteback.analysis.ApplicationClassResolver;
import byteback.analysis.Namespace;
import byteback.converter.soottoboogie.Prelude;
import byteback.converter.soottoboogie.program.ProgramConverter;
import byteback.frontend.boogie.ast.Program;
import soot.SootClass;

public class ConversionTask {

	private final ApplicationClassResolver resolver;

	private Prelude prelude;

	public ConversionTask(final ApplicationClassResolver resolver, final Prelude prelude) {
		this.resolver = resolver;
		this.prelude = prelude;
	}

	public Program run() {
		Program program = new Program();
		
		ProgramConverter.v().convert(resolver).inject(program);
		program = prelude.program().merge(program);
		program.inferModifies();

		return program;
	}

}
