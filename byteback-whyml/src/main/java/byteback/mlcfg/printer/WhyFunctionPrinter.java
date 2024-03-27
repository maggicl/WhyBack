package byteback.mlcfg.printer;

import byteback.mlcfg.identifiers.Identifier;
import static byteback.mlcfg.printer.Statement.block;
import static byteback.mlcfg.printer.Statement.line;
import static byteback.mlcfg.printer.Statement.many;
import byteback.mlcfg.syntax.WhyFunction;
import java.util.Collection;
import java.util.List;

public class WhyFunctionPrinter {
	private final WhySignaturePrinter signaturePrinter;

	public WhyFunctionPrinter(WhySignaturePrinter signaturePrinter) {
		this.signaturePrinter = signaturePrinter;
	}

	public Statement toWhy(WhyFunction f) {
		return block(signaturePrinter.toWhy(f.getSignature()), line("="), f.getBody().toWhy().statement());
	}

	public Statement toWhy(Identifier.FQDN declaringClass, Collection<WhyFunction> functions) {
		final WhyClassScope scope = new WhyClassScope(declaringClass);
		return scope.with(block(functions.stream().map(this::toWhy)));
	}
}
