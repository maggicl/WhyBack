package byteback.whyml.vimp;

import byteback.whyml.syntax.function.CFGBlock;
import byteback.whyml.syntax.function.CFGLabel;
import byteback.whyml.syntax.function.WhyFunctionSignature;
import byteback.whyml.syntax.statement.CFGTerminator;
import byteback.whyml.vimp.expr.CFGTerminatorExtractor;
import byteback.whyml.vimp.expr.ProgramExpressionExtractor;
import byteback.whyml.vimp.expr.ProgramStatementExtractor;
import byteback.whyml.vimp.expr.ProgramLogicalExpressionExtractor;
import java.util.Map;
import java.util.Optional;
import soot.Unit;
import soot.toolkits.graph.Block;

public class VimpBlockParser {

	private final ProgramExpressionExtractor programExpressionExtractor;
	private final ProgramLogicalExpressionExtractor pureProgramExpressionExtractor;
	private final VimpLocalParser vimpLocalParser;
	private final VimpFieldParser fieldParser;
	private final TypeResolver typeResolver;

	public VimpBlockParser(ProgramExpressionExtractor programExpressionExtractor,
						   ProgramLogicalExpressionExtractor pureProgramExpressionExtractor,
						   VimpLocalParser vimpLocalParser,
						   VimpFieldParser fieldParser,
						   TypeResolver typeResolver) {
		this.programExpressionExtractor = programExpressionExtractor;
		this.pureProgramExpressionExtractor = pureProgramExpressionExtractor;
		this.vimpLocalParser = vimpLocalParser;
		this.fieldParser = fieldParser;
		this.typeResolver = typeResolver;
	}

	public CFGBlock parse(WhyFunctionSignature s, Block block, Optional<Unit> fallThrough, Map<Unit, CFGLabel> labelMap) {
		final ProgramStatementExtractor e = new ProgramStatementExtractor(
				programExpressionExtractor,
				pureProgramExpressionExtractor,
				vimpLocalParser,
				fieldParser,
				typeResolver,
				s
		);

		final Unit tail = block.getTail();

		for (Unit u = block.getHead(); u != tail; u = block.getSuccOf(u)) {
			u.apply(e);
		}

		final CFGTerminator terminator = new CFGTerminatorExtractor(programExpressionExtractor, fallThrough, labelMap)
				.visitOrFallThrough(tail);

		return new CFGBlock(labelMap.get(block.getHead()), e.result(), terminator);
	}
}
