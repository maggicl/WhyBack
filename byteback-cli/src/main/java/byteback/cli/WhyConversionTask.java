package byteback.cli;

import byteback.analysis.RootResolver;
import byteback.frontend.boogie.ast.Printable;
import byteback.whyml.IOC;

public class WhyConversionTask implements ConversionTask {

    private final RootResolver resolver;

	public WhyConversionTask(final RootResolver resolver) {
        this.resolver = resolver;
	}

    @Override
    public Printable run() {
        return IOC.PROGRAM_CONVERTER.convert(resolver);
    }

}
