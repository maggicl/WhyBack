package byteback.core.converter.soot.boogie;

import byteback.core.converter.soot.SootLocalExtractor;
import byteback.core.representation.soot.body.SootExpression;
import byteback.core.representation.soot.body.SootStatementVisitor;
import byteback.core.representation.soot.unit.SootMethodUnit;
import byteback.core.util.CountingMap;
import byteback.frontend.boogie.ast.*;
import byteback.frontend.boogie.builder.FunctionDeclarationBuilder;
import byteback.frontend.boogie.builder.FunctionSignatureBuilder;
import byteback.frontend.boogie.builder.OptionalBindingBuilder;
import java.util.Map.Entry;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.*;
import soot.jimple.*;

public class BoogieFunctionExtractor extends SootStatementVisitor<FunctionDeclaration> {

	private static final Logger log = LoggerFactory.getLogger(BoogieFunctionExtractor.class);

	private final SootMethodUnit methodUnit;

	private final FunctionDeclarationBuilder functionBuilder;

	private final FunctionSignatureBuilder signatureBuilder;

	private final CountingMap<Local, Optional<Expression>> expressionIndex;

	public BoogieFunctionExtractor(final SootMethodUnit methodUnit, final FunctionDeclarationBuilder functionBuilder,
			final FunctionSignatureBuilder signatureBuilder) {

		this.methodUnit = methodUnit;
		this.functionBuilder = functionBuilder;
		this.signatureBuilder = signatureBuilder;
		this.expressionIndex = new CountingMap<>();
	}

	@Override
	public void caseIdentityStmt(final IdentityStmt identity) {
		final SootExpression left = new SootExpression(identity.getLeftOp());
		final Local local = new SootLocalExtractor().visit(left);
		final TypeAccess typeAccess = new BoogieTypeAccessExtractor().visit(left.getType());
		final OptionalBinding binding = new OptionalBindingBuilder().name(local.getName()).typeAccess(typeAccess)
				.build();
		signatureBuilder.addInputBinding(binding);
		expressionIndex.put(local, Optional.empty());
	}

	@Override
	public void caseAssignStmt(final AssignStmt assignment) {
		final SootExpression left = new SootExpression(assignment.getLeftOp());
		final SootExpression right = new SootExpression(assignment.getRightOp());
		final Local local = new SootLocalExtractor().visit(left);
		final Expression expression = new BoogieInlineExtractor(left.getType(), expressionIndex).visit(right);
		expressionIndex.put(local, Optional.of(expression));
	}

	@Override
	public void caseReturnStmt(final ReturnStmt returns) {
		final SootExpression operand = new SootExpression(returns.getOp());
		final Expression expression = new BoogieInlineExtractor(methodUnit.getReturnType(), expressionIndex)
				.visit(operand);
		final TypeAccess returnTypeAccess = new BoogieTypeAccessExtractor().visit(methodUnit.getReturnType());
		final OptionalBinding boogieReturnBinding = new OptionalBindingBuilder().typeAccess(returnTypeAccess).build();

		for (Entry<Local, Integer> entry : expressionIndex.getAccessCount().entrySet()) {
			if (entry.getValue() == 0) {
				log.warn("Local assignment {} unused in final expansion", entry.getKey());
			}
		}

		signatureBuilder.outputBinding(boogieReturnBinding);
		functionBuilder.expression(expression);
	}

	@Override
	public void caseDefault(final Unit unit) {
		throw new UnsupportedOperationException("Cannot inline statements of type " + unit.getClass().getName());
	}

	@Override
	public FunctionDeclaration result() {
		return functionBuilder.signature(signatureBuilder.build()).build();
	}

}
