package byteback.whyml.vimp;

import byteback.analysis.util.SootBodies;
import byteback.whyml.identifiers.IdentifierEscaper;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.function.CFGLabel;
import byteback.whyml.syntax.function.WhyLocal;
import byteback.whyml.vimp.expr.PureBodyExtractor;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import soot.Body;
import soot.SootMethod;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.BlockGraph;

public class VimpMethodBodyParser {
	private final IdentifierEscaper identifierEscaper;
	private final TypeResolver typeResolver;
	private final PureBodyExtractor pureBodyExtractor;

	public VimpMethodBodyParser(IdentifierEscaper identifierEscaper,
								TypeResolver typeResolver,
								PureBodyExtractor pureBodyExtractor) {
		this.identifierEscaper = identifierEscaper;
		this.typeResolver = typeResolver;
		this.pureBodyExtractor = pureBodyExtractor;
	}

	public Expression parseSpec(SootMethod method) {
		if (!method.hasActiveBody()) {
			throw new IllegalStateException("Spec method " + method.getSignature() + " has no active body");
		}

		return pureBodyExtractor.visit(method.retrieveActiveBody());
	}

	public Optional<Expression> parseProgram(SootMethod method) {
		if (method.hasActiveBody()) {
			final Body body = method.getActiveBody();

			final List<WhyLocal> locals = body.getLocals().stream()
					.map(e -> new WhyLocal(
							identifierEscaper.escapeL(e.getName()),
							typeResolver.resolveType(e.getType()),
							false
					))
					.toList();

			final BlockGraph bg = SootBodies.getBlockGraph(method.getActiveBody());
			final Map<Block, CFGLabel> labelMap = CFGLabel.forBlocks(bg.getBlocks());

			System.out.println("bg " + bg);

			return Optional.empty(); // todo change
		} else {
			return Optional.empty();
		}
	}
}
