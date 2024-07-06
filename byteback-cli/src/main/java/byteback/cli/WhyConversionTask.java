package byteback.cli;

import byteback.analysis.RootResolver;
import byteback.frontend.boogie.ast.Printable;
import byteback.whyml.IOC;
import byteback.whyml.syntax.HeapKind;

public class WhyConversionTask implements ConversionTask {

    private final RootResolver resolver;
	private final HeapKind kind;

	public WhyConversionTask(final RootResolver resolver, HeapKind kind) {
        this.resolver = resolver;
		this.kind = kind;
	}

    @Override
    public Printable run() {
        return IOC.PROGRAM_CONVERTER.convert(resolver, kind);
    }

}
