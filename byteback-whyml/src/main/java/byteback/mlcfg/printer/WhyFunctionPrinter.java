package byteback.mlcfg.printer;

import byteback.mlcfg.WhyFunctionSCC;
import static byteback.mlcfg.printer.Statement.block;
import static byteback.mlcfg.printer.Statement.many;
import byteback.mlcfg.syntax.WhyFunction;
import byteback.mlcfg.vimp.WhyResolver;
import java.util.List;

public class WhyFunctionPrinter {
	private final WhySignaturePrinter signaturePrinter;

	public WhyFunctionPrinter(WhySignaturePrinter signaturePrinter) {
		this.signaturePrinter = signaturePrinter;
	}

	public Statement toWhy(WhyFunctionSCC scc, WhyResolver resolver) {
		final List<WhyFunction> f = scc.functionList();
		if (f.isEmpty()) return many();

		final Statement[] statements = new Statement[f.size()];
		final boolean recursive = scc.isRecursive();

		statements[0] = defineFunction(f.get(0), false, recursive, resolver);

		for (int i = 1; i < statements.length; i++) {
			statements[i] = defineFunction(f.get(i), true, recursive, resolver);
		}

		return block(statements);
	}

	private Statement defineFunction(WhyFunction f, boolean withWith, boolean recursive, WhyResolver resolver) {
		return many(
				signaturePrinter.toWhy(f.getSignature(), true, withWith, recursive, resolver),
				f.getBody().toWhy().statement("= ", "")
		);
	}
}
