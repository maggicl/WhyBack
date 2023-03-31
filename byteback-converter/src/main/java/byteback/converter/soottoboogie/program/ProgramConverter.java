package byteback.converter.soottoboogie.program;

import byteback.analysis.RootResolver;
import byteback.analysis.Namespace;
import byteback.analysis.transformer.DynamicToStaticTransformer;
import byteback.analysis.transformer.ExceptionInvariantTransformer;
import byteback.analysis.transformer.ExpressionFolder;
import byteback.analysis.transformer.GuardTransformer;
import byteback.analysis.transformer.InvariantExpander;
import byteback.analysis.transformer.LogicUnitTransformer;
import byteback.analysis.transformer.LogicValueTransformer;
import byteback.analysis.transformer.QuantifierValueTransformer;
import byteback.analysis.util.SootBodies;
import byteback.analysis.util.SootHosts;
import byteback.analysis.util.SootMethods;
import byteback.analysis.util.SootBodies.ValidationException;
import byteback.converter.soottoboogie.ConversionException;
import byteback.converter.soottoboogie.field.FieldConverter;
import byteback.converter.soottoboogie.method.function.FunctionManager;
import byteback.converter.soottoboogie.method.procedure.ProcedureConverter;
import byteback.converter.soottoboogie.type.ClassHierarchyConverter;
import byteback.converter.soottoboogie.type.ReferenceTypeConverter;
import byteback.frontend.boogie.ast.AxiomDeclaration;
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

	public void convertFields(final Program program, final RootResolver resolver) {
		for (final SootField field : resolver.getUsedFields()) {
			program.addDeclaration(FieldConverter.instance().convert(field));
		}
	}

	public void transformMethods(final RootResolver resolver) {
		for (final SootMethod method : resolver.getUsedMethods()) {
			if (SootMethods.hasBody(method)) {
				log.info("Transforming method {}", method.getSignature());

				if (SootHosts.hasAnnotation(method, Namespace.PRELUDE_ANNOTATION)) {
					continue;
				}

				try {
					SootBodies.validateCalls(method.retrieveActiveBody());
					final Body body = Grimp.v().newBody(method.getActiveBody(), "");
					LogicUnitTransformer.v().transform(body);
					new LogicValueTransformer(body.getMethod().getReturnType()).transform(body);

					new ExpressionFolder().transform(body);
					UnusedLocalEliminator.v().transform(body);
					QuantifierValueTransformer.v().transform(body);
					ExceptionInvariantTransformer.v().transform(body);
					InvariantExpander.v().transform(body);
					DynamicToStaticTransformer.v().transform(body);

					if (!Namespace.isPureMethod(method) && !Namespace.isPredicateMethod(method)) {
						GuardTransformer.v().transform(body);
					}

					method.setActiveBody(body);
				} catch (final ValidationException e) {
					e.printStackTrace();
					log.warn("Skipping method {}", method.getName());
				}
			}
		}
	}

	public void convertMethods(final Program program, final RootResolver resolver) {
		for (final SootMethod method : resolver.getUsedMethods()) {
			try {
				log.info("Converting method {}", method.getSignature());

				if (SootHosts.hasAnnotation(method, Namespace.PRELUDE_ANNOTATION)) {
					continue;
				}

				if (SootHosts.hasAnnotation(method, Namespace.PURE_ANNOTATION)) {
					program.addDeclaration(FunctionManager.v().convert(method));
				} else if (!SootHosts.hasAnnotation(method, Namespace.PREDICATE_ANNOTATION)) {
					program.addDeclaration(ProcedureConverter.v().convert(method));
				}
			} catch (final ConversionException exception) {
				log.error("Conversion exception:");
				exception.printStackTrace();
				log.warn("Skipping method {}", method.getName());
			}
		}
	}

	public void convertClasses(final Program program, final RootResolver resolver) {
		for (final SootClass clazz : resolver.getUsedClasses()) {
			program.addDeclaration(ReferenceTypeConverter.v().convert(clazz));

			for (final AxiomDeclaration axiomDeclaration : ClassHierarchyConverter.v().convert(clazz)) {
				program.addDeclaration(axiomDeclaration);
			}
		}
	}

	public Program convert(final RootResolver resolver) {
		final var program = new Program();

		convertClasses(program, resolver);
		convertFields(program, resolver);
		transformMethods(resolver);
		convertMethods(program, resolver);

		return program;
	}

}
