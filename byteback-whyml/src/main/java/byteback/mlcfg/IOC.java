package byteback.mlcfg;

import byteback.mlcfg.printer.RecordPrinter;
import byteback.mlcfg.vimpParser.VimpClassParser;

public final class IOC {
    public static final VimpClassParser classParser = new VimpClassParser();
    public static final RecordPrinter recordPrinter = new RecordPrinter();
    public static final ProgramConverter programConverter = new ProgramConverter(classParser, recordPrinter);
    private IOC() {
    }
}
