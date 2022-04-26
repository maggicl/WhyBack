package byteback.core.converter.soottoboogie.method.procedure;

import byteback.core.converter.soottoboogie.ConversionException;
import byteback.core.converter.soottoboogie.Prelude;
import byteback.core.converter.soottoboogie.expression.PartialSubstitutor;
import byteback.core.converter.soottoboogie.expression.Substitutor;
import byteback.core.representation.soot.body.SootBody;
import byteback.core.representation.soot.body.SootStatementVisitor;
import byteback.core.representation.soot.type.SootType;
import byteback.frontend.boogie.ast.Body;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.LabelStatement;
import byteback.frontend.boogie.ast.Statement;
import byteback.frontend.boogie.ast.TypeAccess;
import byteback.frontend.boogie.ast.ValueReference;
import byteback.frontend.boogie.ast.VariableDeclaration;
import java.util.Stack;
import soot.Unit;

public class ProcedureBodyExtractor extends SootStatementVisitor<Body> {

	public class VariableProvider {

		private int variableCounter;

		public VariableProvider() {
			variableCounter = 0;
		}

		public ValueReference get(final TypeAccess typeAccess) {
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

	private final Stack<LoopCollector.LoopContext> activeLoops;

	private final DefinitionCollector definitions;

	private final Substitutor substitutor;

	public ProcedureBodyExtractor(final SootType returnType) {
		this.returnType = returnType;
		this.body = new Body();
		this.variableProvider = new VariableProvider();
		this.labelCollector = new LabelCollector();
		this.loopCollector = new LoopCollector();
		this.definitions = new DefinitionCollector();
		this.activeLoops = new Stack<>();
		this.substitutor = new PartialSubstitutor();
	}

	public Body visit(final SootBody body) {
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
			throw new ConversionException("Trying to insert an invariant outside of a loop context");
		}

		activeLoops.peek().addInvariant(argument);
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
		return substitutor;
	}

	public DefinitionCollector getDefinitionCollector() {
		return definitions;
	}

	@Override
	public void caseDefault(final Unit unit) {
		final var extractor = new ProcedureStatementExtractor(this);
		loopCollector.getByHead(unit).ifPresent((context) -> {
			activeLoops.push(context);
			addStatement(context.getAssertionPoint());
		});
		loopCollector.getByBackJump(unit).ifPresent((context) -> {
			assert context.getLoop() == activeLoops.peek().getLoop();
			addStatement(activeLoops.pop().getAssertionPoint());
		});
		labelCollector.getLabel(unit).ifPresent((label) -> {
			body.addStatement(new LabelStatement(label));
		});
		loopCollector.getByExitTarget(unit).ifPresent((context) -> {
			addStatement(context.getAssumptionPoint());
		});
		loopCollector.getByExit(unit).ifPresent((context) -> {
			addStatement(context.getAssumptionPoint());
		});
		extractor.visit(unit);
	}

	@Override
	public Body result() {
		return body;
	}

}
