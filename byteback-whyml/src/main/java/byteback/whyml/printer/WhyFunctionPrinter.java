package byteback.whyml.printer;

import byteback.whyml.WhyFunctionSCC;
import static byteback.whyml.printer.Statement.block;
import static byteback.whyml.printer.Statement.many;
import byteback.whyml.syntax.function.WhySpecFunction;
import byteback.whyml.vimp.WhyResolver;
import java.util.List;

public class WhyFunctionPrinter {
	private final WhySignaturePrinter signaturePrinter;

	public WhyFunctionPrinter(WhySignaturePrinter signaturePrinter) {
		this.signaturePrinter = signaturePrinter;
	}

	public Statement toWhy(WhyFunctionSCC scc, WhyResolver resolver) {
		final List<WhySpecFunction> f = scc.functionList();
		if (f.isEmpty()) return many();

		final Statement[] statements = new Statement[f.size()];
		final boolean recursive = scc.isRecursive();

		statements[0] = defineFunction(f.get(0), false, recursive, resolver);

		for (int i = 1; i < statements.length; i++) {
			statements[i] = defineFunction(f.get(i), true, recursive, resolver);
		}

		return block(statements);
	}

	private Statement defineFunction(WhySpecFunction f, boolean withWith, boolean recursive, WhyResolver resolver) {
		return many(
				signaturePrinter.toWhy(f.signature(), true, withWith, recursive, resolver),
				f.body().toWhy().statement("= ", "")
		);
	}
}
