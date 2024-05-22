package byteback.whyml.vimp;

import byteback.analysis.util.SootBodies;
import byteback.whyml.syntax.function.CFGBlock;
import byteback.whyml.syntax.function.CFGLabel;
import byteback.whyml.syntax.function.WhyFunctionBody;
import byteback.whyml.syntax.function.WhyFunctionDeclaration;
import byteback.whyml.syntax.function.WhyFunctionSignature;
import byteback.whyml.syntax.function.WhyLocal;
import byteback.whyml.vimp.expr.PureBodyExtractor;
import byteback.whyml.vimp.expr.WhyTranslationErrorPrinter;
import byteback.whyml.vimp.expr.WhyTranslationException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import soot.Body;
import soot.Printer;
import soot.SootMethod;
import soot.Unit;
import soot.toolkits.graph.Block;
import soot.toolkits.graph.BlockGraph;

public class VimpMethodBodyParser {
	private final VimpLocalParser vimpLocalParser;
	private final PureBodyExtractor pureBodyExtractor;
	private final VimpBlockParser blockParser;

	public VimpMethodBodyParser(VimpLocalParser vimpLocalParser,
								PureBodyExtractor pureBodyExtractor,
								VimpBlockParser blockParser) {
		this.vimpLocalParser = vimpLocalParser;
		this.pureBodyExtractor = pureBodyExtractor;
		this.blockParser = blockParser;
	}

	public Optional<? extends WhyFunctionBody> parseBody(WhyFunctionDeclaration decl, WhyFunctionSignature s, SootMethod method) {
		try {
			if (decl.isSpec()) {
				return Optional.of(parseSpecBody(method));
			} else {
				return parseProgram(s, method);
			}
		} catch (WhyTranslationException e) {
			System.err.println("Error translating body of method " + method.getDeclaration() + ": " + e.getMessage());

			final WhyTranslationErrorPrinter printer = new WhyTranslationErrorPrinter(method.getActiveBody(), e);
			Printer.v().setCustomUnitPrinter(ignored -> printer);
			try (StringWriter sw = new StringWriter()) {
				try (PrintWriter pw = new PrintWriter(sw)) {
					Printer.v().printTo(method.getActiveBody(), pw);
				}

				System.err.print(sw.toString().replaceAll("\n\n+", "\n"));
			} catch (IOException ex) {
				throw new RuntimeException("Error printing why translation error", ex);
			}

			throw new IllegalStateException("Error translating body of method: " + method.getDeclaration(), e);
		}
	}

	public WhyFunctionBody.SpecBody parseSpecBody(SootMethod method) {
		if (!method.hasActiveBody()) {
			throw new IllegalStateException("Spec method " + method.getSignature() + " has no active body");
		}

		return new WhyFunctionBody.SpecBody(pureBodyExtractor.visit(method.retrieveActiveBody()));
	}

	public Optional<WhyFunctionBody.CFGBody> parseProgram(WhyFunctionSignature s, SootMethod method) {
		if (method.hasActiveBody()) {
			final Body body = method.getActiveBody();

			final List<WhyLocal> locals = body.getLocals().stream()
					.map(vimpLocalParser::parse)
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

				blocks.add(blockParser.parse(s, b, fallThrough, labelMap));
			}

			return Optional.of(new WhyFunctionBody.CFGBody(locals, blocks));
		} else {
			return Optional.empty();
		}
	}
}
