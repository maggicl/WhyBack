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

		statements[0] = defineFunction(f.get(0), false, recursive);

		for (int i = 1; i < statements.length; i++) {
			statements[i] = defineFunction(f.get(i), true, recursive);
		}

		return block(statements);
	}

	private Statement defineFunction(WhySpecFunction f, boolean withWith, boolean recursive) {
		return many(
				signaturePrinter.toWhy(f.contract(), true, withWith, recursive),
				f.body().toWhy().statement("= ", "")
		);
	}
}
