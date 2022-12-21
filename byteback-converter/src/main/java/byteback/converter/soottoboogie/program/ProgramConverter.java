package byteback.converter.soottoboogie.program;

import byteback.analysis.Namespace;
import byteback.analysis.transformer.ExceptionInvariantTransformer;
import byteback.analysis.transformer.ExpressionFolder;
import byteback.analysis.transformer.GuardTransformer;
import byteback.analysis.transformer.InvariantExpander;
import byteback.analysis.transformer.LogicUnitTransformer;
import byteback.analysis.transformer.LogicValueTransformer;
import byteback.analysis.transformer.QuantifierValueTransformer;
import byteback.analysis.util.SootMethods;
import byteback.converter.soottoboogie.ConversionException;
import byteback.converter.soottoboogie.field.FieldConverter;
import byteback.converter.soottoboogie.method.function.FunctionConverter;
import byteback.converter.soottoboogie.method.procedure.ProcedureConverter;
import byteback.converter.soottoboogie.type.ReferenceTypeConverter;
import byteback.frontend.boogie.ast.Program;
import byteback.util.Lazy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import soot.Body;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.grimp.Grimp;
import soot.toolkits.scalar.UnusedLocalEliminator;

public class ProgramConverter {

	public static Logger log = LoggerFactory.getLogger(ProgramConverter.class);

	private static final Lazy<ProgramConverter> instance = Lazy.from(ProgramConverter::new);

	public static ProgramConverter v() {
		return instance.get();
	}

	private ProgramConverter() {
	}

	public static void convertFields(final Program program, final SootClass clazz) {
		for (SootField field : clazz.getFields()) {
			program.addDeclaration(FieldConverter.instance().convert(field));
		}
	}

	public static void convertMethods(final Program program, final SootClass clazz) {
		for (SootMethod method : clazz.getMethods()) {
			if (method.getName().equals("<clinit>")) {
				continue;
			}

			if (SootMethods.hasBody(method)) {
				final Body body = Grimp.v().newBody(method.retrieveActiveBody(), "");
				LogicUnitTransformer.v().transform(body);
				new LogicValueTransformer(body.getMethod().getReturnType()).transform(body);
				new ExpressionFolder().transform(body);
				UnusedLocalEliminator.v().transform(body);
				QuantifierValueTransformer.v().transform(body);
				ExceptionInvariantTransformer.v().transform(body);
				InvariantExpander.v().transform(body);
				GuardTransformer.v().transform(body);
				method.setActiveBody(body);
			}

			try {
				log.info("Converting method {}", method.getSignature());

				if (SootMethods.hasAnnotation(method, Namespace.PURE_ANNOTATION)) {
					program.addDeclaration(FunctionConverter.v().convert(method));
				} else if (!SootMethods.hasAnnotation(method, Namespace.PREDICATE_ANNOTATION)) {
					program.addDeclaration(ProcedureConverter.v().convert(method));
				}

				log.info("Method {} converted", method.getSignature());
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
		convertFields(program, clazz);
		convertMethods(program, clazz);

		return program;
	}

}
