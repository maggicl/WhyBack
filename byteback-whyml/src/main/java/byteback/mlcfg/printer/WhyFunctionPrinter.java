package byteback.mlcfg.printer;

import byteback.mlcfg.WhyFunctionSCC;
import static byteback.mlcfg.printer.Statement.block;
import static byteback.mlcfg.printer.Statement.many;
import byteback.mlcfg.syntax.WhyFunction;
import java.util.List;

public class WhyFunctionPrinter {
	private final WhySignaturePrinter signaturePrinter;

	public WhyFunctionPrinter(WhySignaturePrinter signaturePrinter) {
		this.signaturePrinter = signaturePrinter;
	}

	public Statement toWhy(WhyFunctionSCC scc) {
		final List<WhyFunction> f = scc.functionList();
		if (f.isEmpty()) return many();

		final Statement[] statements = new Statement[f.size()];

		statements[0] = many(
				signaturePrinter.toWhy(f.get(0).getSignature(), true, false),
				f.get(0).getBody().toWhy().statement("= ", "")
		);

		for (int i = 1; i < statements.length; i++) {
			statements[i] = many(
					signaturePrinter.toWhy(f.get(i).getSignature(), true, true),
					f.get(i).getBody().toWhy().statement("= ", "")
			);
		}

		return block(statements);
	}
}
