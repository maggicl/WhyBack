package byteback.whyml.printer;

import byteback.whyml.WhyFunctionSCC;
import static byteback.whyml.printer.Code.block;
import static byteback.whyml.printer.Code.many;
import byteback.whyml.syntax.function.WhySpecFunction;
import byteback.whyml.vimp.WhyResolver;
import java.util.List;

public class WhyFunctionPrinter {
	private final WhySignaturePrinter signaturePrinter;

	public WhyFunctionPrinter(WhySignaturePrinter signaturePrinter) {
		this.signaturePrinter = signaturePrinter;
	}

	public Code toWhy(WhyFunctionSCC scc, WhyResolver resolver) {
		final List<WhySpecFunction> f = scc.functionList();
		if (f.isEmpty()) return many();

		final Code[] lines = new Code[f.size()];
		final boolean recursive = scc.isRecursive();

		lines[0] = defineFunction(f.get(0), false, recursive);

		for (int i = 1; i < lines.length; i++) {
			lines[i] = defineFunction(f.get(i), true, recursive);
		}

		return block(lines);
	}

	private Code defineFunction(WhySpecFunction f, boolean withWith, boolean recursive) {
		return many(
				signaturePrinter.toWhy(f.contract(), true, withWith, recursive),
				f.body().toWhy().statement("= ", "")
		);
	}
}
