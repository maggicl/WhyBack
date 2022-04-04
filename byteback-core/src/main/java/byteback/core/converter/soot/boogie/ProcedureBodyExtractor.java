package byteback.core.converter.soot.boogie;

import byteback.core.representation.soot.body.SootStatementVisitor;
import byteback.core.representation.soot.type.SootType;
import byteback.frontend.boogie.ast.Body;
import byteback.frontend.boogie.ast.Label;
import byteback.frontend.boogie.ast.LabelStatement;
import byteback.frontend.boogie.ast.List;
import byteback.frontend.boogie.ast.LoopInvariant;
import byteback.frontend.boogie.ast.Statement;
import byteback.frontend.boogie.ast.ValueReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;
import soot.Unit;
import soot.jimple.toolkits.annotation.logic.Loop;

public class ProcedureBodyExtractor extends SootStatementVisitor<Body> {

  public static class LoopContext {

    private final Loop loop;

    private final List<LoopInvariant> invariants;

    public LoopContext(final Loop loop, final List<LoopInvariant> invariants) {
      this.loop = loop;
      this.invariants = invariants;
    }

    public Loop getLoop() {
      return loop;
    }

    public List<LoopInvariant> getInvariants() {
      return invariants;
    }

  }

	private final Body body;

	private final SootType returnType;

	private final LabelCollector labelCollector;

	private final LoopCollector loopCollector;

	private final Map<Unit, Statement> statementTable;

	private final Supplier<ValueReference> referenceSupplier;

  private final Stack<LoopContext> loopContexts;

	public Supplier<ValueReference> makeReferenceSupplier() {
		final AtomicInteger counter = new AtomicInteger();
		counter.set(0);

		return () -> Prelude.generateVariableReference(counter.incrementAndGet());
	}

	public ProcedureBodyExtractor(final Body body, final SootType returnType, final LabelCollector labelCollector,
			final LoopCollector loopCollector) {
		this.body = body;
		this.returnType = returnType;
		this.labelCollector = labelCollector;
		this.loopCollector = loopCollector;
		this.referenceSupplier = makeReferenceSupplier();
		this.statementTable = new HashMap<>();
    this.loopContexts = new Stack<>();
	}

	@Override
	public void caseDefault(final Unit unit) {
		final var extractor = new StatementExtractor(body, returnType, labelCollector, referenceSupplier);
    final Optional<Loop> loopStart = loopCollector.getByStart(unit);
    final Optional<Loop> loopEnd = loopCollector.getByEnd(unit);
    final Optional<Label> labelLookup = labelCollector.getLabel(unit);

    if (loopStart.isPresent()) {
      final Loop loop = loopStart.get();
      loopContexts.push(new LoopContext(loop, new List<>()));
    }

    if (loopEnd.isPresent()) {
      final LoopContext loopContext = loopContexts.pop();
      assert loopContext.getLoop() == loopEnd.get();
    }

		if (labelLookup.isPresent()) {
			body.addStatement(new LabelStatement(labelLookup.get()));
		}

		unit.apply(extractor);
    statementTable.put(unit, extractor.result());
	}

	@Override
	public Body result() {
		return body;
	}

}
