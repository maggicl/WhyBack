package byteback.converter.soottoboogie.program;

import byteback.analysis.ApplicationClassResolver;
import byteback.analysis.Namespace;
import byteback.analysis.transformer.ExceptionInvariantTransformer;
import byteback.analysis.transformer.ExpressionFolder;
import byteback.analysis.transformer.GuardTransformer;
import byteback.analysis.transformer.InvariantExpander;
import byteback.analysis.transformer.LogicUnitTransformer;
import byteback.analysis.transformer.LogicValueTransformer;
import byteback.analysis.transformer.QuantifierValueTransformer;
import byteback.analysis.util.SootBodies;
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

import java.util.HashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.Body;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.grimp.Grimp;
import soot.toolkits.scalar.UnusedLocalEliminator;
import soot.util.Chain;
import soot.util.HashChain;

public class ProgramConverter {

	public static Logger log = LoggerFactory.getLogger(ProgramConverter.class);

	private static final Lazy<ProgramConverter> instance = Lazy.from(ProgramConverter::new);

	public static ProgramConverter v() {
		return instance.get();
	}

	private final Set<SootMethod> converted;

	private ProgramConverter() {
		converted = new HashSet<>();
	}

	public void convertFields(final Program program, final ApplicationClassResolver resolver, final SootClass clazz) {
		for (final SootField field : clazz.getFields()) {
			if (resolver.isUsed(field)) {
				program.addDeclaration(FieldConverter.instance().convert(field));
			}
		}
	}

	public void transformMethods(final SootClass clazz) {
		for (final SootMethod method : clazz.getMethods()) {

			log.info("Transforming method {}", method.getSignature());

			try {
				SootBodies.validateCalls(method.retrieveActiveBody());
				final Body body = Grimp.v().newBody(method.getActiveBody(), "");
				LogicUnitTransformer.v().transform(body);
				new LogicValueTransformer(body.getMethod().getReturnType()).transform(body);

				if (!Namespace.isPureMethod(method) && !Namespace.isPredicateMethod(method)) {
					GuardTransformer.v().transform(body);
				}

				new ExpressionFolder().transform(body);
				UnusedLocalEliminator.v().transform(body);
				QuantifierValueTransformer.v().transform(body);
				ExceptionInvariantTransformer.v().transform(body);
				InvariantExpander.v().transform(body);
				method.setActiveBody(body);
			} catch (final ValidationException e) {
				e.printStackTrace();
				log.warn("Skipping method {}", method.getName());
			}
		}
	}

	public void convertMethods(final Program program, final ApplicationClassResolver resolver, final SootClass clazz) {
		transformMethods(clazz);

		for (final SootMethod method : clazz.getMethods()) {

			if (!resolver.isUsed(method)) {
				continue;
			}

			try {
				log.info("Converting method {}", method.getSignature());

				if (SootMethods.hasAnnotation(method, Namespace.PURE_ANNOTATION)) {
					program.addDeclaration(FunctionManager.v().convert(method));
				} else if (!SootMethods.hasAnnotation(method, Namespace.PREDICATE_ANNOTATION)) {
					program.addDeclaration(ProcedureConverter.v().convert(method));
				}
			} catch (final ConversionException exception) {
				log.error("Conversion exception:");
				exception.printStackTrace();
				log.warn("Skipping method {}", method.getName());
			}

			converted.add(method);
		}
	}

	public Program convert(final ApplicationClassResolver resolver) {
		final var program = new Program();

		for (final SootClass clazz : resolver.getClasses()) {
			log.info("Converting class {}", clazz.getName());

			program.addDeclaration(ReferenceTypeConverter.v().convert(clazz));

			for (final AxiomDeclaration axiomDeclaration : ClassHierarchyConverter.v().convert(clazz)) {
				program.addDeclaration(axiomDeclaration);
			}

			convertFields(program, resolver, clazz);
			convertMethods(program, resolver, clazz);
		}

		return program;
	}

}
