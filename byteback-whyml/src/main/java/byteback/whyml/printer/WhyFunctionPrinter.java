package byteback.whyml.printer;

import static byteback.whyml.printer.Code.block;
import static byteback.whyml.printer.Code.many;
import byteback.whyml.syntax.function.WhyFunction;
import byteback.whyml.syntax.function.WhyFunctionBody;
import byteback.whyml.syntax.function.WhyFunctionSignature;
import byteback.whyml.vimp.graph.SCC;
import java.util.List;

public class WhyFunctionPrinter {
	private final WhySignaturePrinter signaturePrinter;

	public WhyFunctionPrinter(WhySignaturePrinter signaturePrinter) {
		this.signaturePrinter = signaturePrinter;
	}

	public Code toWhy(SCC<WhyFunctionSignature, WhyFunction> scc) {
		final List<WhyFunction> f = scc.elements();
		if (f.isEmpty()) return many();

		final Code[] lines = new Code[f.size()];
		final boolean recursive = scc.hasLoop();

		lines[0] = defineFunction(f.get(0), false, recursive);

		for (int i = 1; i < lines.length; i++) {
			lines[i] = defineFunction(f.get(i), true, recursive);
		}

		return block(lines);
	}

	private Code defineFunction(WhyFunction f, boolean withWith, boolean recursive) {
		return many(
				signaturePrinter.toWhy(f, withWith, recursive),
				many(f.body().stream().map(WhyFunctionBody::toWhy))
		);
	}
}
