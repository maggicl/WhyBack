package byteback.cli;

import byteback.analysis.RootResolver;
import byteback.converter.soottoboogie.Prelude;
import byteback.converter.soottoboogie.program.ProgramConverter;
import byteback.frontend.boogie.ast.Printable;
import byteback.frontend.boogie.ast.Program;

public class BoogieConversionTask implements ConversionTask {

    private final RootResolver resolver;

    private final Prelude prelude;

    public BoogieConversionTask(final RootResolver resolver, final Prelude prelude) {
        this.resolver = resolver;
        this.prelude = prelude;
    }

    public Printable run() {
        Program program = new Program();

        ProgramConverter.v().convert(resolver).inject(program);
        program = prelude.program().merge(program);
        program.inferModifies();

        return program;
    }

}
