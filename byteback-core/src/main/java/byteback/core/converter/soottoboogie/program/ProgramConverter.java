package byteback.core.converter.soottoboogie.program;

import byteback.core.converter.soottoboogie.Annotations;
import byteback.core.converter.soottoboogie.Prelude;
import byteback.core.converter.soottoboogie.field.FieldConverter;
import byteback.core.converter.soottoboogie.method.function.FunctionConverter;
import byteback.core.converter.soottoboogie.method.procedure.ProcedureConverter;
import byteback.core.representation.soot.unit.SootClass;
import byteback.frontend.boogie.ast.Program;
import java.util.stream.Stream;

public class ProgramConverter {

	private static final ProgramConverter instance = new ProgramConverter();

	public static ProgramConverter instance() {
		return instance;
	}

	public Program convert(final SootClass clazz) {
		final var program = new Program();
		clazz.fields().forEach((field) -> program.addDeclaration(FieldConverter.instance().convert(field)));
		clazz.methods().forEach((method) -> {
			if (method.getAnnotation(Annotations.PURE_ANNOTATION).isPresent()) {
				program.addDeclaration(FunctionConverter.instance().convert(method));
			} else if (method.getAnnotation(Annotations.CONDITION_ANNOTATION).isEmpty()) {
				program.addDeclaration(ProcedureConverter.instance().convert(method));
			}
		});

		return program;
	}

	public Program convert(final Stream<SootClass> classes) {
		final var program = new Program();
		classes.forEach((clazz) -> program.merge(convert(clazz)));

		return Prelude.loadProgram().merge(program);
	}

}
