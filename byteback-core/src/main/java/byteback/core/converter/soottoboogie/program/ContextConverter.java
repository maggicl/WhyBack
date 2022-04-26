package byteback.core.converter.soottoboogie.program;

import byteback.core.context.soot.SootContext;
import byteback.core.converter.soottoboogie.AnnotationNamespace;
import byteback.frontend.boogie.ast.Program;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ContextConverter {

	public static Logger log = LoggerFactory.getLogger(ContextConverter.class);

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
				.convert(context.classes().filter((clazz) -> !AnnotationNamespace.isAnnotationClass(clazz)
						&& !clazz.isBasicClass() && !clazz.isPhantomClass()));
		program.inferModifies();

		return program;
	}

}
