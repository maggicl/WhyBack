package byteback.whyml.vimp;

import byteback.analysis.util.SootBodies;
import byteback.whyml.identifiers.IdentifierEscaper;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.function.CFGBlock;
import byteback.whyml.syntax.function.CFGLabel;
import byteback.whyml.syntax.function.WhyFunctionBody;
import byteback.whyml.syntax.function.WhyFunctionDeclaration;
import byteback.whyml.syntax.function.WhyLocal;
import byteback.whyml.vimp.expr.PureBodyExtractor;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import soot.Body;
import soot.SootMethod;
import soot.Unit;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.BlockGraph;

public class VimpMethodBodyParser {
	private final IdentifierEscaper identifierEscaper;
	private final TypeResolver typeResolver;
	private final PureBodyExtractor pureBodyExtractor;
	private final VimpBlockParser blockParser;

	public VimpMethodBodyParser(IdentifierEscaper identifierEscaper,
								TypeResolver typeResolver,
								PureBodyExtractor pureBodyExtractor,
								VimpBlockParser blockParser) {
		this.identifierEscaper = identifierEscaper;
		this.typeResolver = typeResolver;
		this.pureBodyExtractor = pureBodyExtractor;
		this.blockParser = blockParser;
	}

	public Optional<? extends WhyFunctionBody> parse(WhyFunctionDeclaration decl, SootMethod method) {
		if (decl.isSpec()) {
			return Optional.of(new WhyFunctionBody.SpecBody(parseSpec(method)));
		} else {
			return parseProgram(method);
		}
	}

	private Expression parseSpec(SootMethod method) {
		if (!method.hasActiveBody()) {
			throw new IllegalStateException("Spec method " + method.getSignature() + " has no active body");
		}

		return pureBodyExtractor.visit(method.retrieveActiveBody());
	}

	public Optional<WhyFunctionBody.CFGBody> parseProgram(SootMethod method) {
		if (method.hasActiveBody()) {
			final Body body = method.getActiveBody();

			final List<WhyLocal> locals = body.getLocals().stream()
					.map(e -> new WhyLocal(
							identifierEscaper.escapeL(e.getName()),
							typeResolver.resolveType(e.getType())
					))
					.toList();

			final BlockGraph bg = SootBodies.getBlockGraph(method.getActiveBody());
			final Map<Unit, CFGLabel> labelMap = CFGLabel.forBlocks(
					bg.getBlocks().stream()
							.map(Block::getHead)
							.toList());

			final List<Block> sootBlocks = bg.getBlocks();
			final List<CFGBlock> blocks = new ArrayList<>();

			for (int i = 0; i < sootBlocks.size(); i++) {
				final Block b = sootBlocks.get(i);
				final Optional<Unit> fallThrough = i + 1 < sootBlocks.size()
						? Optional.of(sootBlocks.get(i + 1).getHead())
						: Optional.empty();

				blocks.add(blockParser.parse(b, fallThrough, labelMap));
			}

			return Optional.of(new WhyFunctionBody.CFGBody(locals, blocks));
		} else {
			return Optional.empty();
		}
	}
}
