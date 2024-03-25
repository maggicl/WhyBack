package byteback.mlcfg.printer;

import byteback.mlcfg.identifiers.Identifier;
import static byteback.mlcfg.printer.Statement.block;
import static byteback.mlcfg.printer.Statement.line;
import static byteback.mlcfg.printer.Statement.many;
import byteback.mlcfg.syntax.expr.WhyFunction;
import java.util.List;

public class WhyFunctionPrinter {
	private final WhyFunctionSignaturePrinter signaturePrinter;

	public WhyFunctionPrinter(WhyFunctionSignaturePrinter signaturePrinter) {
		this.signaturePrinter = signaturePrinter;
	}

	public Statement toWhy(WhyFunction f) {
		return many(signaturePrinter.toWhy(f.getSignature()), line("="), line(f.getBody().toWhy()));
	}

	public Statement toWhy(Identifier.FQDN declaringClass, List<WhyFunction> functions) {
		final WhyClassScope scope = new WhyClassScope(declaringClass);
		return scope.with(block(functions.stream().map(this::toWhy)));
	}
}
