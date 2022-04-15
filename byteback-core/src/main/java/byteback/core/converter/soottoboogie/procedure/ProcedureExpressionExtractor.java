package byteback.core.converter.soottoboogie.procedure;

import byteback.core.converter.soottoboogie.Annotations;
import byteback.core.converter.soottoboogie.NameConverter;
import byteback.core.converter.soottoboogie.Prelude;
import byteback.core.converter.soottoboogie.expression.SubstitutingExtractor;
import byteback.core.representation.soot.annotation.SootAnnotation;
import byteback.core.representation.soot.body.SootExpression;
import byteback.core.representation.soot.type.SootType;
import byteback.core.representation.soot.unit.SootMethod;
import byteback.frontend.boogie.ast.Accessor;
import byteback.frontend.boogie.ast.AssertStatement;
import byteback.frontend.boogie.ast.AssumeStatement;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.List;
import byteback.frontend.boogie.ast.Procedure;
import byteback.frontend.boogie.ast.ProcedureDeclaration;
import byteback.frontend.boogie.ast.TargetedCallStatement;
import byteback.frontend.boogie.ast.ValueReference;
import java.util.Collections;
import java.util.Iterator;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;
import soot.BooleanType;
import soot.Local;
import soot.Unit;
import soot.jimple.NewExpr;
import soot.jimple.SpecialInvokeExpr;

public class ProcedureExpressionExtractor extends SubstitutingExtractor {

	public interface VariableSupplier extends Supplier<ValueReference> {
	}

	final ProcedureBodyExtractor bodyExtractor;

	final VariableSupplier variableSupplier;

	final Unit unit;

	public ProcedureExpressionExtractor(final ProcedureBodyExtractor bodyExtractor, final VariableSupplier supplier,
			final Unit unit) {
		super(bodyExtractor.getSubstitutor());
		this.bodyExtractor = bodyExtractor;
		this.variableSupplier = supplier;
		this.unit = unit;
	}

	public TargetedCallStatement makeCall(final SootMethod method, final Iterable<SootExpression> arguments) {
    final ProcedureDeclaration procedureDeclaration = ProcedureManager.instance().convert(method);
    final Procedure procedure = procedureDeclaration.getProcedure();
		final TargetedCallStatement call = procedure.makeTargetedCall();
		call.setArgumentList(new List<Expression>().addAll(convertArguments(method, arguments)));

    if (Prelude.modifiesHeap(procedure)) {
      bodyExtractor.setModifiesHeap();
    }

    return call;
	}

	public void addCall(final TargetedCallStatement callStatement, final Iterable<SootExpression> arguments) {
		final List<ValueReference> targets = new List<ValueReference>();

		if (variableSupplier != null) {
			final ValueReference reference = variableSupplier.get();
			targets.add(reference);
			pushExpression(reference);
		}

		callStatement.setTargetList(targets);
		bodyExtractor.addStatement(callStatement);
	}

	public void addSpecial(final SootMethod method, final Iterable<SootExpression> arguments) {
		final Iterator<SootExpression> iterator = arguments.iterator();
		final SootExpression argument = iterator.next();
		final Expression condition = visit(argument, new SootType(BooleanType.v()));
		assert !iterator.hasNext() : "Wrong number of arguments to special method";

		if (method.equals(Annotations.ASSERT_METHOD.get())) {
			bodyExtractor.addStatement(new AssertStatement(condition));
		} else if (method.equals(Annotations.ASSUME_METHOD.get())) {
			bodyExtractor.addStatement(new AssumeStatement(condition));
		} else if (method.equals(Annotations.INVARIANT_METHOD.get())) {
			bodyExtractor.addInvariant(condition);
		} else {
			throw new RuntimeException("Unknown special method: " + method.getName());
		}
	}

	@Override
	public void pushFunctionReference(final SootMethod method, final Iterable<SootExpression> arguments) {
		final Optional<SootAnnotation> annotation = method.getAnnotation(Annotations.PURE_ANNOTATION);

		if (annotation.isPresent()) {
			super.pushFunctionReference(method, arguments);
		} else if (method.getSootClass().equals(Annotations.CONTRACT_CLASS.get())) {
			addSpecial(method, arguments);
		} else {
			final TargetedCallStatement callStatement = makeCall(method, arguments);
			addCall(callStatement, arguments);
		}
	}

	@Override
	public void caseSpecialInvokeExpr(final SpecialInvokeExpr invoke) {
		final var method = new SootMethod(invoke.getMethod());
		final var base = new SootExpression(invoke.getBase());
		final Iterable<SootExpression> arguments = Stream.concat(Stream.of(base),
				invoke.getArgs().stream().map(SootExpression::new))::iterator;
		pushFunctionReference(method, arguments);
	}

	@Override
	public void caseNewExpr(final NewExpr newExpression) {
		final Procedure newProcedure = Prelude.getNewProcedure();
		final TargetedCallStatement callStatement = newProcedure.makeTargetedCall();
		addCall(callStatement, Collections.emptyList());
	}

	@Override
	public void caseLocal(final Local local) {
		if (!bodyExtractor.getDefinitionCollector().hasSingleDefinition(local, unit)) {
			pushExpression(ValueReference.of(local.getName()));
		} else {
			super.caseLocal(local);
		}
	}

}
