package byteback.core.converter.soottoboogie.program;

import byteback.core.converter.soottoboogie.ConversionException;
import byteback.core.converter.soottoboogie.Namespace;
import byteback.core.converter.soottoboogie.Prelude;
import byteback.core.converter.soottoboogie.field.FieldConverter;
import byteback.core.converter.soottoboogie.method.function.FunctionConverter;
import byteback.core.converter.soottoboogie.method.procedure.ProcedureConverter;
import byteback.core.converter.soottoboogie.type.ReferenceTypeConverter;
import byteback.core.representation.soot.unit.SootMethods;
import byteback.frontend.boogie.ast.Program;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;

public class ProgramConverter {

	public static Logger log = LoggerFactory.getLogger(ProgramConverter.class);

	private static final ProgramConverter instance = new ProgramConverter();

	public static ProgramConverter instance() {
		return instance;
	}

	public static void convertFields(final Program program, final SootClass clazz) {
		for (SootField field : clazz.getFields()) {
			program.addDeclaration(FieldConverter.instance().convert(field));
		}
	}

	public static void convertMethods(final Program program, final SootClass clazz) {
		for (SootMethod method : clazz.getMethods()) {
			System.out.println(method);
			try {
				log.info("Converting method {}", method.getSignature());
				if (SootMethods.hasAnnotation(method, Namespace.PURE_ANNOTATION)) {
					program.addDeclaration(FunctionConverter.v().convert(method));
				} else if (!SootMethods.hasAnnotation(method, Namespace.PREDICATE_ANNOTATION)) {
					program.addDeclaration(ProcedureConverter.v().convert(method));
				}
			} catch (final ConversionException exception) {
				log.error("Conversion exception:");
				exception.printStackTrace();
				log.warn("Skipping method {}", method.getName());
			}
		}
	}

	public Program convert(final SootClass clazz) {
		log.info("Converting class {}", clazz.getName());
		final var program = new Program();
		program.addDeclaration(ReferenceTypeConverter.instance().convert(clazz));
		System.out.println("Converting fields");
		convertFields(program, clazz);
		System.out.println("Converting methods");
		convertMethods(program, clazz);

		return program;
	}

	public Program convert(final Stream<SootClass> classes) {
		final Program program = Prelude.v().program();
		classes.forEach((clazz) -> {
			try {
				System.out.println(clazz);
				convert(clazz).inject(program);
			} catch (final ConversionException exception) {
				log.error("Conversion exception: ");
				exception.printStackTrace();
				log.warn("Skipping class " + clazz.getName());
			}
		});

		return program;
	}

}
