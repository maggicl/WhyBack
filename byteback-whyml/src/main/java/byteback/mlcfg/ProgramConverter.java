package byteback.mlcfg;

import byteback.analysis.RootResolver;
import byteback.mlcfg.printer.RecordPrinter;
import byteback.mlcfg.syntax.WhyProgram;
import byteback.mlcfg.vimpParser.VimpClassParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ProgramConverter {
    public static Logger log = LoggerFactory.getLogger(ProgramConverter.class);
    private final VimpClassParser classParser;
    private final RecordPrinter recordPrinter;

    public ProgramConverter(VimpClassParser classParser, RecordPrinter recordPrinter) {
        this.classParser = classParser;
        this.recordPrinter = recordPrinter;
    }

    public String convertClasses(final RootResolver resolver) {
        return StreamSupport.stream(resolver.getUsedClasses().spliterator(), false)
                .flatMap(classParser::parseClassDeclaration)
                .map(recordPrinter::printRecord)
                .collect(Collectors.joining("\n\n"));
    }

    public WhyProgram convert(final RootResolver resolver) {
        return new WhyProgram(convertClasses(resolver));
    }
}
