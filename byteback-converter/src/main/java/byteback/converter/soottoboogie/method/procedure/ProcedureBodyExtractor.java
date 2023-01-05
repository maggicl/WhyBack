package byteback.converter.soottoboogie.method.procedure;

import byteback.analysis.JimpleStmtSwitch;
import byteback.converter.soottoboogie.Convention;
import byteback.converter.soottoboogie.type.TypeAccessExtractor;
import byteback.frontend.boogie.ast.Body;
import byteback.frontend.boogie.ast.Label;
import byteback.frontend.boogie.ast.LabelStatement;
import byteback.frontend.boogie.ast.Statement;
import byteback.frontend.boogie.ast.TypeAccess;
import byteback.frontend.boogie.ast.ValueReference;
import byteback.frontend.boogie.ast.VariableDeclaration;
import soot.Type;
import soot.Unit;

public class ProcedureBodyExtractor extends JimpleStmtSwitch<Body> {

	public class VariableProvider {

		private int variableCounter;

		public VariableProvider() {
			variableCounter = 0;
		}

		public ValueReference get(final TypeAccess typeAccess) {
			final ValueReference reference = Convention.makeValueReference(++variableCounter);
			final VariableDeclaration declaration = reference.makeVariableDeclaration(typeAccess);
			body.addLocalDeclaration(declaration);

			return reference;
		}

	}

	private final Body body;

	private final VariableProvider variableProvider;

	private final LabelCollector labelCollector;

	public ProcedureBodyExtractor() {
		this.body = new Body();
		this.variableProvider = new VariableProvider();
		this.labelCollector = new LabelCollector();
	}

	public Body visit(final soot.Body body) {
		labelCollector.collect(body);

		return super.visit(body);
	}

	public void addStatement(final Statement statement) {
		body.addStatement(statement);
	}

	public void addLocalDeclaration(final VariableDeclaration declaration) {
		body.addLocalDeclaration(declaration);
	}

	public Body getBody() {
		return body;
	}

	public LabelCollector getLabelCollector() {
		return labelCollector;
	}

	public VariableProvider getVariableProvider() {
		return variableProvider;
	}

	public ValueReference generateReference(final Type type) {
		final TypeAccess access = new TypeAccessExtractor().visit(type);
		
		return variableProvider.get(access);
	}

	@Override
	public void caseDefault(final Unit unit) {
		if (labelCollector.hasLabel(unit)) {
			final Label label = labelCollector.fetchLabel(unit);
			addStatement(new LabelStatement(label));
		}

		final var extractor = new ProcedureStatementExtractor(this);
		extractor.visit(unit);
	}

	@Override
	public Body result() {
		return body;
	}

}
