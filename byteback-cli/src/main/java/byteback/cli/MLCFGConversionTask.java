package byteback.cli;

import byteback.analysis.RootResolver;
import byteback.frontend.boogie.ast.Printable;
import byteback.mlcfg.IOC;

public class MLCFGConversionTask implements ConversionTask {

    private final RootResolver resolver;

    public MLCFGConversionTask(final RootResolver resolver) {
        this.resolver = resolver;
    }

    @Override
    public Printable run() {
        return IOC.programConverter.convert(resolver);
    }

}
