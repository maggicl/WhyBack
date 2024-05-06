package byteback.whyml.vimp;

import byteback.whyml.syntax.function.CFGBlock;
import byteback.whyml.syntax.function.CFGLabel;
import byteback.whyml.syntax.statement.CFGTerminator;
import byteback.whyml.vimp.expr.CFGTerminatorExtractor;
import byteback.whyml.vimp.expr.ProcedureExpressionExtractor;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import soot.Unit;
import soot.toolkits.graph.Block;

public class VimpBlockParser {

	private final ProcedureExpressionExtractor procedureExpressionExtractor;

	public VimpBlockParser(ProcedureExpressionExtractor procedureExpressionExtractor) {
		this.procedureExpressionExtractor = procedureExpressionExtractor;
	}

	public CFGBlock parse(Block block, Optional<Unit> fallThrough, Map<Unit, CFGLabel> labelMap) {
		final Unit tail = block.getTail();
		final CFGTerminator terminator = new CFGTerminatorExtractor(procedureExpressionExtractor, fallThrough, labelMap)
				.visitOrFallThrough(tail);

		// TODO: complete
		return new CFGBlock(labelMap.get(block.getHead()), List.of(), terminator);
	}
}
