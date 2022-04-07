package byteback.core.converter.soot.boogie;

import byteback.core.representation.soot.annotation.SootAnnotation;
import byteback.core.representation.soot.body.SootExpression;
import byteback.core.representation.soot.type.SootType;
import byteback.core.representation.soot.unit.SootMethod;
import byteback.frontend.boogie.ast.Accessor;
import byteback.frontend.boogie.ast.AssertStatement;
import byteback.frontend.boogie.ast.AssumeStatement;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.List;
import byteback.frontend.boogie.ast.SymbolicReference;
import byteback.frontend.boogie.ast.TargetedCallStatement;
import byteback.frontend.boogie.ast.ValueReference;
import java.util.Optional;
import java.util.function.Supplier;
import soot.BooleanType;
import soot.Local;
import soot.Unit;

public class ProcedureExpressionExtractor extends ExpressionExtractor {

	public static interface VariableSupplier extends Supplier<ValueReference> {
	}

	final ProcedureBodyExtractor bodyExtractor;

	final VariableSupplier variableSupplier;

	final Unit unit;

	public ProcedureExpressionExtractor(final ProcedureBodyExtractor bodyExtractor, final VariableSupplier supplier,
			final Unit unit) {
		this.bodyExtractor = bodyExtractor;
		this.variableSupplier = supplier;
		this.unit = unit;
	}

	public TargetedCallStatement makeCall(final SootMethod method, final Iterable<SootExpression> arguments) {
		final String methodName = NameConverter.methodName(method);
		final TargetedCallStatement call = new TargetedCallStatement();
		call.setAccessor(new Accessor(methodName));
		call.setArgumentList(new List<Expression>().addAll(convertArguments(method, arguments)));

		return call;
	}

	public void addCall(final SootMethod method, final Iterable<SootExpression> arguments) {
		final TargetedCallStatement callStatement = makeCall(method, arguments);
		final List<SymbolicReference> targets = new List<SymbolicReference>();

		if (variableSupplier != null) {
			final ValueReference reference = variableSupplier.get();
			targets.add(reference);
			pushExpression(reference);
		}

		callStatement.setTargetList(targets);
		bodyExtractor.addStatement(callStatement);
	}

	public void addSpecial(final SootMethod method, final Iterable<SootExpression> arguments) {
		final SootExpression argument = arguments.iterator().next();
		final Expression boogieArgument = new SubstitutingExtractor(bodyExtractor.getSubstituter()) {

			@Override
			public void caseLocal(final Local local) {
				if (!bodyExtractor.getDefinitionCollector().hasSingleDefinition(local, unit)) {
					throw new IllegalStateException(
							"Cannot substitute local " + local + " because its definition is ambiguous");
				}

				super.caseLocal(local);
			}

		}.visit(argument, new SootType(BooleanType.v()));

		if (method.equals(Annotations.ASSERT_METHOD)) {
			bodyExtractor.addStatement(new AssertStatement(boogieArgument));
		} else if (method.equals(Annotations.ASSUME_METHOD)) {
			bodyExtractor.addStatement(new AssumeStatement(boogieArgument));
		} else if (method.equals(Annotations.INVARIANT_METHOD)) {
			bodyExtractor.addInvariant(boogieArgument);
		} else {
			throw new RuntimeException("Unknown special method: " + method.getName());
		}
	}

	@Override
	public void pushFunctionReference(final SootMethod method, final Iterable<SootExpression> arguments) {
		final Optional<SootAnnotation> annotation = method.getAnnotation(Annotations.PURE_ANNOTATION);

		if (annotation.isPresent()) {
			super.pushFunctionReference(method, arguments);
		} else if (method.getSootClass().equals(Annotations.CONTRACT_CLASS)) {
			addSpecial(method, arguments);
		} else {
			addCall(method, arguments);
		}
	}

}
