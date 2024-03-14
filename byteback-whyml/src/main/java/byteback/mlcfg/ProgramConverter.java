package byteback.mlcfg;

import byteback.analysis.RootResolver;
import byteback.mlcfg.printer.Statement;
import static byteback.mlcfg.printer.Statement.block;
import static byteback.mlcfg.printer.Statement.line;
import static byteback.mlcfg.printer.Statement.many;
import byteback.mlcfg.printer.WhyClassDeclaration;
import byteback.mlcfg.printer.WhyClassPrinter;
import byteback.mlcfg.printer.WhyMethodPrinter;
import byteback.mlcfg.syntax.WhyProgram;
import byteback.mlcfg.vimp.VimpClassParser;
import byteback.mlcfg.vimp.VimpMethodParser;
import byteback.mlcfg.vimp.WhyResolver;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProgramConverter {
	public static Logger log = LoggerFactory.getLogger(ProgramConverter.class);
	private final VimpClassParser classParser;

	private final VimpMethodParser methodParser;
	private final WhyClassPrinter whyClassPrinter;

	private final WhyMethodPrinter whyMethodPrinter;

	public ProgramConverter(VimpClassParser classParser, VimpMethodParser methodParser, WhyClassPrinter whyClassPrinter, WhyMethodPrinter whyMethodPrinter) {
		this.classParser = classParser;
		this.methodParser = methodParser;
		this.whyClassPrinter = whyClassPrinter;
		this.whyMethodPrinter = whyMethodPrinter;
	}

	public WhyResolver resolve(final RootResolver resolver) {
		final WhyResolver whyResolver = new WhyResolver();

		StreamSupport.stream(resolver.getUsedClasses().spliterator(), false)
				.map(classParser::parse)
				.forEach(whyResolver::addClass);

		StreamSupport.stream(resolver.getUsedMethods().spliterator(), false)
				.map(methodParser::parse)
				.forEach(whyResolver::addMethod);

		return whyResolver;
	}

	public WhyProgram convert(final RootResolver resolver) {
		final WhyResolver whyResolver = resolve(resolver);

		final List<WhyClassDeclaration> decls = whyResolver.classes()
				.map(e -> whyClassPrinter.toWhy(e, whyResolver))
				.toList();

		final List<Statement> methodDecls = whyResolver.methods()
				.map(e -> whyMethodPrinter.toWhy(e.getKey(), e.getValue()))
				.toList();

		return new WhyProgram(many(
				block(line("(* class type hierarchy declaration *)")),
				block(decls.stream().map(WhyClassDeclaration::typeDeclaration)),
				block(line("(* class field declaration *)")),
				block(decls.stream().map(WhyClassDeclaration::fieldDeclaration).flatMap(Optional::stream)),
				block(line("(* methods declaration *)")),
				block(methodDecls.stream())
		));
	}
}
