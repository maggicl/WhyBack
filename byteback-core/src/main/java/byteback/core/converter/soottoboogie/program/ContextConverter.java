package byteback.core.converter.soottoboogie.program;

import byteback.core.context.soot.SootContext;
import byteback.frontend.boogie.ast.Program;

public class ContextConverter {

	private static final ContextConverter instance = new ContextConverter(SootContext.instance());

	public static ContextConverter instance() {
		return instance;
	}

	final SootContext context;

	public ContextConverter(final SootContext context) {
		this.context = context;
	}

	public Program convert() {
		final Program program = ProgramConverter.instance()
				.convert(context.classes().filter((clazz) -> !clazz.isBasicClass() && !clazz.isPhantomClass()));

		return program;
	}

}
