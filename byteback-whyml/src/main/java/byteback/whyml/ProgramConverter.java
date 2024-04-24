package byteback.whyml;

import byteback.analysis.RootResolver;
import byteback.whyml.printer.Statement;
import static byteback.whyml.printer.Statement.block;
import static byteback.whyml.printer.Statement.line;
import static byteback.whyml.printer.Statement.many;
import byteback.whyml.printer.WhyClassDeclaration;
import byteback.whyml.printer.WhyClassPrinter;
import byteback.whyml.printer.WhyFunctionPrinter;
import byteback.whyml.printer.WhySignaturePrinter;
import byteback.whyml.syntax.WhyProgram;
import byteback.whyml.syntax.function.WhyFunctionDeclaration;
import byteback.whyml.vimp.VimpClassParser;
import byteback.whyml.vimp.VimpMethodBodyParser;
import byteback.whyml.vimp.VimpMethodParser;
import byteback.whyml.vimp.WhyResolver;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import soot.SootClass;
import soot.SootMethod;

public class ProgramConverter {
	private final WhyResolver whyResolver;
	private final WhyClassPrinter whyClassPrinter;
	private final WhySignaturePrinter whySignaturePrinter;
	private final WhyFunctionPrinter whyFunctionPrinter;

	public ProgramConverter(WhyResolver resolver,
							WhyClassPrinter whyClassPrinter,
							WhySignaturePrinter whySignaturePrinter,
							WhyFunctionPrinter whyFunctionPrinter) {
		this.whyResolver = resolver;
		this.whyClassPrinter = whyClassPrinter;
		this.whySignaturePrinter = whySignaturePrinter;
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

		final List<Statement> functionDecls = whyResolver.specFunctions().stream()
				.map(e -> whyFunctionPrinter.toWhy(e, whyResolver))
				.toList();

		final List<Statement> methodDecls = whyResolver.methodDeclarations()
				.map(e -> whySignaturePrinter.toWhy(e.getKey(), e.getValue(), whyResolver))
				.toList();

		return new WhyProgram(many(
				block(line("(* class type hierarchy declaration *)")),
				block(decls.stream().map(WhyClassDeclaration::typeDeclaration)),
				block(line("(* class field declaration *)")),
				block(decls.stream().map(WhyClassDeclaration::fieldDeclaration).flatMap(Optional::stream)),
				block(line("(* spec declaration *)")),
				block(functionDecls.stream()),
				block(line("(* method signature declaration *)")),
				block(methodDecls.stream()),
				block(line("(* method bodies declaration *)")),
				block(line("(* TODO *)"))
		));
	}
}
