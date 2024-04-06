package byteback.mlcfg.printer;

import byteback.mlcfg.identifiers.Identifier;
import static byteback.mlcfg.printer.Statement.block;
import static byteback.mlcfg.printer.Statement.indent;
import static byteback.mlcfg.printer.Statement.line;
import static byteback.mlcfg.printer.Statement.many;
import byteback.mlcfg.syntax.WhyFunctionKind;
import byteback.mlcfg.syntax.WhyFunctionParam;
import byteback.mlcfg.syntax.WhyFunctionSignature;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class WhySignaturePrinter {

	public WhySignaturePrinter() {
	}

	public Statement toWhy(WhyFunctionSignature m, boolean forScc, boolean withWith, boolean recursive) {
		if (forScc && !m.kind().isSpec()) {
			throw new IllegalArgumentException("SCC signature mode supported only for spec functionList");
		}

		final List<WhyFunctionParam> paramsList = m.params().toList();

		final String params = paramsList.stream()
				.map(e -> "(%s: %s)".formatted(e.name(), e.type().getWhyType()))
				.collect(Collectors.joining(" "));

		final Statement paramPreconditions = many(paramsList.stream()
				.map(WhyFunctionParam::condition)
				.flatMap(Optional::stream)
				.map(e -> line("requires { %s }".formatted(e))));

		final Statement resultPostcondition = many(
				new WhyFunctionParam(Identifier.Special.RESULT, m.returnType(), false)
						.condition()
						.map("ensures { %s }"::formatted)
						.map(Statement::line)
						.stream());

		// TODO: capture variants
		final Statement variant = forScc && recursive ? line("variant { 0 }") : many();

		final String declaration;

		if (withWith) {
			declaration = "with";
		} else {
			final WhyFunctionKind kindToUse = forScc ? WhyFunctionKind.PURE_FUNCTION : m.kind();
			declaration = recursive
					? kindToUse.getWhyRecDeclaration()
					: kindToUse.getWhyDeclaration();
		}

		final String returnType = m.kind() != WhyFunctionKind.PREDICATE || forScc
				? " : %s".formatted(m.returnType().getWhyType())
				: "";

		return many(
				line("%s %s (ghost heap: Heap.t) %s%s".formatted(
						declaration,
						forScc ? m.specName() : m.name(),
						params,
						returnType
				)),
				indent(
						paramPreconditions,
						resultPostcondition,
						variant
				)
		);
	}

	public Statement toWhy(WhyFunctionSignature m) {
		return toWhy(m, false, false, false);
	}

	public Statement toWhy(Identifier.FQDN declaringClass, List<WhyFunctionSignature> methods) {
		final WhyClassScope scope = new WhyClassScope(declaringClass);
		return scope.with(block(methods.stream().map(this::toWhy).map(Statement::block)));
	}
}
