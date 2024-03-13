package byteback.mlcfg;

import byteback.analysis.RootResolver;
import byteback.mlcfg.printer.Statement;
import static byteback.mlcfg.printer.Statement.many;
import byteback.mlcfg.printer.WhyClassPrinter;
import byteback.mlcfg.syntax.WhyProgram;
import byteback.mlcfg.vimpParser.VimpClassParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.StreamSupport;

public class ProgramConverter {
    public static Logger log = LoggerFactory.getLogger(ProgramConverter.class);
    private final VimpClassParser classParser;
    private final WhyClassPrinter whyClassPrinter;

    public ProgramConverter(VimpClassParser classParser, WhyClassPrinter whyClassPrinter) {
        this.classParser = classParser;
        this.whyClassPrinter = whyClassPrinter;
    }

    public Statement convertClasses(final RootResolver resolver) {
        return many(
                StreamSupport.stream(resolver.getUsedClasses().spliterator(), false)
                .map(classParser::parseClassDeclaration)
                .map(whyClassPrinter::toWhy));
    }

    public WhyProgram convert(final RootResolver resolver) {
        return new WhyProgram(convertClasses(resolver));
    }
}
