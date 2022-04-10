package byteback.core.converter.soottoboogie.procedure;

import byteback.core.converter.soottoboogie.Prelude;
import byteback.core.converter.soottoboogie.expression.Substitutor;
import byteback.core.converter.soottoboogie.procedure.LoopCollector.LoopContext;
import byteback.core.representation.soot.body.SootBody;
import byteback.core.representation.soot.body.SootStatementVisitor;
import byteback.core.representation.soot.type.SootType;
import byteback.frontend.boogie.ast.Body;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.Label;
import byteback.frontend.boogie.ast.LabelStatement;
import byteback.frontend.boogie.ast.Statement;
import byteback.frontend.boogie.ast.TypeAccess;
import byteback.frontend.boogie.ast.ValueReference;
import byteback.frontend.boogie.ast.VariableDeclaration;
import java.util.Optional;
import java.util.Stack;
import java.util.function.Function;
import soot.Unit;

public class ProcedureBodyExtractor extends SootStatementVisitor<Body> {

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

	private final Stack<LoopContext> activeLoops;

	private final DefinitionCollector definitions;

	private final Substitutor substitutions;

	private boolean modifiesHeap;

	public ProcedureBodyExtractor(final SootType returnType) {
		this.returnType = returnType;
		this.body = new Body();
		this.variableProvider = new VariableProvider();
		this.labelCollector = new LabelCollector();
		this.loopCollector = new LoopCollector();
		this.definitions = new DefinitionCollector();
		this.activeLoops = new Stack<>();
		this.substitutions = new Substitutor();
		this.modifiesHeap = false;
	}

	public Body visit(final SootBody body) {
		System.out.println(body);
		labelCollector.collect(body);
		loopCollector.collect(body);
		definitions.collect(body);
		body.apply(this);

		return result();
	}

	public void addStatement(final Statement statement) {
		body.addStatement(statement);
	}

	public void addInvariant(final Expression argument) {
		if (activeLoops.isEmpty()) {
			throw new IllegalStateException("Trying to insert an invariant outside of a loop context");
		}

		activeLoops.peek().addInvariant(argument);
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

	public Substitutor getSubstitutor() {
		return substitutions;
	}

	public DefinitionCollector getDefinitionCollector() {
		return definitions;
	}

	public void setModifiesHeap() {
		this.modifiesHeap = true;
	}

	public boolean getModifiesHeap() {
		return modifiesHeap;
	}

	@Override
	public void caseDefault(final Unit unit) {
		final var extractor = new ProcedureStatementExtractor(this);
		final Optional<LoopContext> loopContextStart = loopCollector.getByHead(unit);
		final Optional<LoopContext> loopContextEnd = loopCollector.getByBackJump(unit);
		final Optional<LoopContext> loopContextExit = loopCollector.getByExitTarget(unit);
		final Optional<Label> labelLookup = labelCollector.getLabel(unit);

		if (loopContextStart.isPresent()) {
			final LoopContext loopContext = loopContextStart.get();
			activeLoops.push(loopContext);
			addStatement(loopContext.getAssertionPoint());
		}

		if (loopContextEnd.isPresent()) {
			final LoopContext loopContext = activeLoops.pop();
			assert loopContext.getLoop() == loopContextEnd.get().getLoop();
			addStatement(loopContext.getAssertionPoint());
		}

		if (labelLookup.isPresent()) {
			body.addStatement(new LabelStatement(labelLookup.get()));
		}

		if (loopContextExit.isPresent()) {
			final LoopContext loopContext = loopContextExit.get();
			addStatement(loopContext.getAssumptionPoint());
		}

		unit.apply(extractor);
	}

	@Override
	public Body result() {
		return body;
	}

}
