package byteback.mlcfg;

import byteback.analysis.RootResolver;
import byteback.mlcfg.printer.Statement;
import static byteback.mlcfg.printer.Statement.block;
import static byteback.mlcfg.printer.Statement.line;
import static byteback.mlcfg.printer.Statement.many;
import byteback.mlcfg.printer.WhyClassDeclaration;
import byteback.mlcfg.printer.WhyClassPrinter;
import byteback.mlcfg.printer.WhyFunctionPrinter;
import byteback.mlcfg.printer.WhySignaturePrinter;
import byteback.mlcfg.syntax.WhyFunctionSignature;
import byteback.mlcfg.syntax.WhyProgram;
import byteback.mlcfg.vimp.VimpClassParser;
import byteback.mlcfg.vimp.VimpMethodBodyParser;
import byteback.mlcfg.vimp.VimpMethodParser;
import byteback.mlcfg.vimp.WhyResolver;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.SootMethod;

public class ProgramConverter {
	public static Logger log = LoggerFactory.getLogger(ProgramConverter.class);
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

		final List<Map.Entry<WhyFunctionSignature, SootMethod>> sigs =
				StreamSupport.stream(resolver.getUsedMethods().spliterator(), false)
						.flatMap(e -> methodParser.parseSignature(e).stream().map(sig -> Map.entry(sig, e)))
						.toList();

		sigs.stream()
				.filter(s -> s.getKey().kind().isSpec())
				.map(s -> methodBodyParser.parse(s.getKey(), s.getValue()))
				.forEach(whyResolver::addFunction);

		sigs.stream()
				.filter(s -> !s.getKey().kind().isSpec())
				.map(Map.Entry::getKey)
				.forEach(whyResolver::addMethod);

		return whyResolver;
	}

	public WhyProgram convert(final RootResolver resolver) {
		final WhyResolver whyResolver = resolve(resolver);

		final List<WhyClassDeclaration> decls = whyResolver.classes()
				.map(e -> whyClassPrinter.toWhy(e, whyResolver))
				.toList();

		final List<Statement> functionDecls = whyResolver.functions()
				.map(e -> whyFunctionPrinter.toWhy(e.getKey(), e.getValue()))
				.toList();

		final List<Statement> methodDecls = whyResolver.methods()
				.map(e -> whySignaturePrinter.toWhy(e.getKey(), e.getValue()))
				.toList();

		return new WhyProgram(many(
				block(line("(* class type hierarchy declaration *)")),
				block(decls.stream().map(WhyClassDeclaration::typeDeclaration)),
				block(line("(* class field declaration *)")),
				block(decls.stream().map(WhyClassDeclaration::fieldDeclaration).flatMap(Optional::stream)),
				block(line("(* methods declaration *)")),
				block(functionDecls.stream()),
				block(methodDecls.stream())
		));
	}
}
