package byteback.core.converter.soottoboogie.program;

import byteback.core.converter.soottoboogie.Namespace;
import byteback.core.converter.soottoboogie.Prelude;
import byteback.core.converter.soottoboogie.field.FieldConverter;
import byteback.core.converter.soottoboogie.method.function.FunctionConverter;
import byteback.core.converter.soottoboogie.method.procedure.ProcedureConverter;
import byteback.core.converter.soottoboogie.type.ReferenceTypeConverter;
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
		program.addDeclaration(ReferenceTypeConverter.instance().convert(clazz));
		clazz.fields().forEach((field) -> program.addDeclaration(FieldConverter.instance().convert(field)));
		clazz.methods().forEach((method) -> {
			if (method.annotation(Namespace.PURE_ANNOTATION).isPresent()) {
				program.addDeclaration(FunctionConverter.instance().convert(method));
			} else if (method.annotation(Namespace.PREDICATE_ANNOTATION).isEmpty()) {
				program.addDeclaration(ProcedureConverter.instance().convert(method));
			}
		});

		return program;
	}

	public Program convert(final Stream<SootClass> classes) {
		final Program program = Prelude.instance().program();
		classes.forEach((clazz) -> convert(clazz).inject(program));

		return program;
	}

}
