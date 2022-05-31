package byteback.core.converter.soottoboogie.program;

import byteback.core.converter.soottoboogie.ConversionException;
import byteback.core.converter.soottoboogie.Namespace;
import byteback.core.converter.soottoboogie.Prelude;
import byteback.core.converter.soottoboogie.field.FieldConverter;
import byteback.core.converter.soottoboogie.method.function.FunctionConverter;
import byteback.core.converter.soottoboogie.method.procedure.ProcedureConverter;
import byteback.core.converter.soottoboogie.type.ReferenceTypeConverter;
import byteback.core.representation.soot.unit.SootClass;
import byteback.frontend.boogie.ast.Program;

import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProgramConverter {

	public static Logger log = LoggerFactory.getLogger(ProgramConverter.class);

	private static final ProgramConverter instance = new ProgramConverter();

	public static ProgramConverter instance() {
		return instance;
	}

	public Program convert(final SootClass clazz) {
		final var program = new Program();
		program.addDeclaration(ReferenceTypeConverter.instance().convert(clazz));
		clazz.fields().forEach((field) -> program.addDeclaration(FieldConverter.instance().convert(field)));
		clazz.methods().forEach((method) -> {
			try {
				if (method.annotation(Namespace.PURE_ANNOTATION).isPresent()) {
					program.addDeclaration(FunctionConverter.instance().convert(method));
				} else if (method.annotation(Namespace.PREDICATE_ANNOTATION).isEmpty()) {
					program.addDeclaration(ProcedureConverter.instance().convert(method));
				}
			} catch (final ConversionException exception) {
				log.error("Conversion exception: ");
				System.err.println(exception);
				log.warn("Skipping method " + method.getName());
			}
		});

		return program;
	}

	public Program convert(final Stream<SootClass> classes) {
		final Program program = Prelude.instance().program();
		classes.forEach((clazz) -> {
			try {
				convert(clazz).inject(program);
			} catch (final ConversionException exception) {
				log.error("Conversion exception: ");
				System.err.println(exception);
				log.warn("Skipping class " + clazz.getName());
			}
		});

		return program;
	}

}
