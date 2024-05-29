package byteback.whyml;

import byteback.analysis.RootResolver;
import byteback.whyml.printer.Code;
import static byteback.whyml.printer.Code.block;
import static byteback.whyml.printer.Code.line;
import static byteback.whyml.printer.Code.many;
import byteback.whyml.printer.WhyClassDeclaration;
import byteback.whyml.printer.WhyClassPrinter;
import byteback.whyml.printer.WhyFunctionPrinter;
import byteback.whyml.syntax.WhyProgram;
import byteback.whyml.vimp.WhyResolver;
import java.util.List;
import java.util.Optional;
import soot.SootClass;
import soot.SootMethod;

public class ProgramConverter {
	private final WhyResolver whyResolver;
	private final WhyClassPrinter whyClassPrinter;
	private final WhyFunctionPrinter whyFunctionPrinter;

	public ProgramConverter(WhyResolver resolver,
							WhyClassPrinter whyClassPrinter,
							WhyFunctionPrinter whyFunctionPrinter) {
		this.whyResolver = resolver;
		this.whyClassPrinter = whyClassPrinter;
		this.whyFunctionPrinter = whyFunctionPrinter;
	}

	public void resolveAll(final RootResolver rootResolver) {
		whyResolver.resolveAllConditionData(rootResolver.getConditions());

		for (final SootClass c : rootResolver.getUsedClasses()) {
			whyResolver.resolveClass(c);
		}

		for (final SootMethod m : rootResolver.getUsedMethods()) {
			whyResolver.resolveMethod(m);
		}
	}

	public WhyProgram convert(final RootResolver resolver) {
		resolveAll(resolver);

		final List<WhyClassDeclaration> decls = whyResolver.classes().stream()
				.map(e -> whyClassPrinter.toWhy(e, whyResolver))
				.toList();

		final List<Code> functionDecls = whyResolver.functions().stream()
				.map(e -> whyFunctionPrinter.toWhy(e, whyResolver))
				.toList();

		return new WhyProgram(many(
				block(line("(* class type hierarchy declaration *)")),
				block(decls.stream().map(WhyClassDeclaration::typeDeclaration)),
				block(line("(* class field declaration *)")),
				block(decls.stream().map(WhyClassDeclaration::fieldDeclaration).flatMap(Optional::stream)),
				block(line("(* methods *)")),
				block(functionDecls.stream())
		));
	}
}
