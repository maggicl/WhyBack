package byteback.cli;

import byteback.analysis.RootResolver;
import byteback.frontend.boogie.ast.Printable;
import byteback.whyml.IOC;

public class WhyConversionTask implements ConversionTask {

    private final RootResolver resolver;
    private final boolean useMLCFG;

    public WhyConversionTask(final RootResolver resolver, boolean useMLCFG) {
        this.resolver = resolver;
		this.useMLCFG = useMLCFG;
	}

    @Override
    public Printable run() {
        return IOC.PROGRAM_CONVERTER.convert(resolver, useMLCFG);
    }

}
