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
import byteback.whyml.vimp.VimpClassParser;
import byteback.whyml.syntax.function.VimpMethod;
import byteback.whyml.vimp.VimpMethodBodyParser;
import byteback.whyml.vimp.VimpMethodParser;
import byteback.whyml.vimp.WhyResolver;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import soot.SootMethod;

public class ProgramConverter {
	private final VimpClassParser classParser;
	private final VimpMethodParser methodParser;
	private final VimpMethodBodyParser methodBodyParser;
	private final WhyClassPrinter whyClassPrinter;
	private final WhySignaturePrinter whySignaturePrinter;
	private final WhyFunctionPrinter whyFunctionPrinter;

	public ProgramConverter(VimpClassParser classParser, VimpMethodParser methodParser,
							VimpMethodBodyParser methodBodyParser, WhyClassPrinter whyClassPrinter,
							WhySignaturePrinter whySignaturePrinter,
							WhyFunctionPrinter whyFunctionPrinter) {
		this.classParser = classParser;
		this.methodParser = methodParser;
		this.methodBodyParser = methodBodyParser;
		this.whyClassPrinter = whyClassPrinter;
		this.whySignaturePrinter = whySignaturePrinter;
		this.whyFunctionPrinter = whyFunctionPrinter;
	}

	public WhyResolver resolve(final RootResolver resolver) {
		final WhyResolver whyResolver = new WhyResolver();

		StreamSupport.stream(resolver.getUsedClasses().spliterator(), false)
				.map(classParser::parse)
				.forEach(whyResolver::addClass);

		for (final SootMethod m : resolver.getUsedMethods()) {
			final Optional<VimpMethod> refOpt = methodParser.reference(m);
			if (refOpt.isEmpty()) continue;

			final VimpMethod ref = refOpt.get();

			methodParser.signature(ref, m).ifPresent(e -> whyResolver.addSpecSignature(ref, e));

			if (ref.kind().isSpec()) {
				whyResolver.addSpecBody(ref, methodBodyParser.parse(m));
			}
		}

		return whyResolver;
	}

	public WhyProgram convert(final RootResolver resolver) {
		final WhyResolver whyResolver = resolve(resolver);

		final List<WhyClassDeclaration> decls = whyResolver.classes().stream()
				.map(e -> whyClassPrinter.toWhy(e, whyResolver))
				.toList();

		final List<Statement> functionDecls = whyResolver.functions().stream()
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
