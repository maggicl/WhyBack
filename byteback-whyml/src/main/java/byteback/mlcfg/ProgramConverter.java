package byteback.mlcfg;

import byteback.analysis.RootResolver;
import static byteback.mlcfg.printer.Statement.many;
import byteback.mlcfg.printer.WhyClassPrinter;
import byteback.mlcfg.syntax.WhyProgram;
import byteback.mlcfg.vimp.VimpClassParser;
import byteback.mlcfg.vimp.WhyResolver;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProgramConverter {
	public static Logger log = LoggerFactory.getLogger(ProgramConverter.class);
	private final VimpClassParser classParser;
	private final WhyClassPrinter whyClassPrinter;

	public ProgramConverter(VimpClassParser classParser, WhyClassPrinter whyClassPrinter) {
		this.classParser = classParser;
		this.whyClassPrinter = whyClassPrinter;
	}

	public WhyResolver resolve(final RootResolver resolver) {
		final WhyResolver whyResolver = new WhyResolver();

		StreamSupport.stream(resolver.getUsedClasses().spliterator(), false)
				.map(classParser::parseClassDeclaration)
				.forEach(whyResolver::add);

		return whyResolver;
	}

	public WhyProgram convert(final RootResolver resolver) {
		final WhyResolver whyResolver = resolve(resolver);
		return new WhyProgram(many(whyResolver.stream().map(e -> whyClassPrinter.toWhy(e, whyResolver))));
	}
}
