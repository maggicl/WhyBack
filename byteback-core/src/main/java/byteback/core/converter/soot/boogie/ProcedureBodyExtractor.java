package byteback.core.converter.soot.boogie;

import byteback.core.representation.soot.body.SootBody;
import byteback.core.representation.soot.body.SootStatementVisitor;
import byteback.core.representation.soot.type.SootType;
import byteback.frontend.boogie.ast.Body;
import byteback.frontend.boogie.ast.Label;
import byteback.frontend.boogie.ast.LabelStatement;
import byteback.frontend.boogie.ast.List;
import byteback.frontend.boogie.ast.LoopInvariant;
import byteback.frontend.boogie.ast.Statement;
import byteback.frontend.boogie.ast.TypeAccess;
import byteback.frontend.boogie.ast.ValueReference;
import byteback.frontend.boogie.ast.VariableDeclaration;
import java.util.Optional;
import java.util.Stack;
import java.util.function.Function;
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

	public class VariableProvider implements Function<TypeAccess, ValueReference> {

		private int variableCounter;

		public VariableProvider() {
			variableCounter = 0;
		}

		public ValueReference apply(final TypeAccess typeAccess) {
			final ValueReference reference = Prelude.makeValueReference(++variableCounter);
			final VariableDeclaration declaration = reference.makeVariableDeclaration(typeAccess);
			body.addLocalDeclaration(declaration);

			return reference;
		}

	}

	private final SootType returnType;

	private final Body body;

	private final VariableProvider variableProvider;

	private final LabelCollector labelCollector;

	private final LoopCollector loopCollector;

	private final Stack<LoopContext> loopContexts;

	public ProcedureBodyExtractor(final SootType returnType) {
		this.returnType = returnType;
		this.body = new Body();
		this.variableProvider = new VariableProvider();
		this.labelCollector = new LabelCollector();
		this.loopCollector = new LoopCollector();
		this.loopContexts = new Stack<>();
	}

	public Body visit(final SootBody body) {
		labelCollector.collect(body);
		loopCollector.collect(body);

		return super.visit(body);
	}

	public void addStatement(final Statement statement) {
		body.addStatement(statement);
	}

	public ValueReference addLocal(final TypeAccess typeAccess) {
		return variableProvider.apply(typeAccess);
	}

	public Body getBody() {
		return body;
	}

	public SootType getReturnType() {
		return returnType;
	}

	public LoopCollector getLoopCollector() {
		return loopCollector;
	}

	public LabelCollector getLabelCollector() {
		return labelCollector;
	}

	public VariableProvider getVariableProvider() {
		return variableProvider;
	}

	@Override
	public void caseDefault(final Unit unit) {
		final var extractor = new StatementExtractor(this);
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
	}

	@Override
	public Body result() {
		return body;
	}

}
