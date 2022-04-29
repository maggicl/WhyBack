package byteback.core.converter.soottoboogie.method.procedure;

import byteback.core.converter.soottoboogie.*;
import byteback.core.converter.soottoboogie.expression.SubstitutingExtractor;
import byteback.core.converter.soottoboogie.method.MethodConverter;
import byteback.core.converter.soottoboogie.method.procedure.ProcedureStatementExtractor.ReferenceSupplier;
import byteback.core.representation.soot.body.SootExpression;
import byteback.core.representation.soot.type.SootType;
import byteback.core.representation.soot.unit.SootClass;
import byteback.core.representation.soot.unit.SootMethod;
import byteback.frontend.boogie.ast.AssertStatement;
import byteback.frontend.boogie.ast.AssumeStatement;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.List;
import byteback.frontend.boogie.ast.Procedure;
import byteback.frontend.boogie.ast.TargetedCallStatement;
import byteback.frontend.boogie.ast.ValueReference;
import byteback.frontend.boogie.builder.TargetedCallStatementBuilder;
import java.util.Iterator;
import java.util.Optional;
import soot.Local;
import soot.Unit;
import soot.jimple.NewExpr;
import soot.jimple.SpecialInvokeExpr;

public class ProcedureExpressionExtractor extends SubstitutingExtractor {

	private static final ReferenceSupplier EMPTY_REFERENCE_SUPPLIER = () -> Optional.empty();

	final ProcedureBodyExtractor bodyExtractor;

	final ReferenceSupplier referenceSupplier;

	final Unit unit;

	public ProcedureExpressionExtractor(final ProcedureBodyExtractor bodyExtractor,
			final ReferenceSupplier referenceSupplier, final Unit unit) {
		super(bodyExtractor.getSubstitutor());
		this.bodyExtractor = bodyExtractor;
		this.referenceSupplier = referenceSupplier;
		this.unit = unit;
	}

	public ProcedureExpressionExtractor(final ProcedureBodyExtractor bodyExtractor, final Unit unit) {
		this(bodyExtractor, EMPTY_REFERENCE_SUPPLIER, unit);
	}

	public TargetedCallStatement makeCall(final SootMethod method, final Iterable<SootExpression> arguments) {
		final var callBuilder = new TargetedCallStatementBuilder();
		callBuilder.name(MethodConverter.methodName(method));
		callBuilder.arguments(new List<Expression>().addAll(convertArguments(method, arguments)));

		return callBuilder.build();
	}

	public void addCall(final TargetedCallStatement callStatement) {
		final List<ValueReference> targets = new List<ValueReference>();
		referenceSupplier.get().ifPresent((reference) -> {
			targets.add(reference);
			pushExpression(reference);
		});

		callStatement.setTargetList(targets);
		bodyExtractor.addStatement(callStatement);
	}

	public void addContract(final SootMethod method, final Iterable<SootExpression> arguments) {
		final String name = method.getName();
		final Iterator<SootExpression> iterator = arguments.iterator();
		final SootExpression argument = iterator.next();
		final Expression condition = visit(argument, SootType.booleanType());
		assert !iterator.hasNext() : "Wrong number of arguments to contract method";

		if (name.equals(Namespace.ASSERTION_NAME)) {
			bodyExtractor.addStatement(new AssertStatement(condition));
		} else if (name.equals(Namespace.ASSUMPTION_NAME)) {
			bodyExtractor.addStatement(new AssumeStatement(condition));
		} else if (name.equals(Namespace.INVARIANT_NAME)) {
			bodyExtractor.addInvariant(condition);
		} else {
			throw new ConversionException("Unknown special method: " + method.getName());
		}
	}

	@Override
	public void pushFunctionReference(final SootMethod method, final Iterable<SootExpression> arguments) {
		final SootClass clazz = method.getSootClass();
		final boolean isPure = method.annotation(Namespace.PURE_ANNOTATION).isPresent();

		if (isPure) {
			super.pushFunctionReference(method, arguments);
		} else if (Namespace.isContractClass(clazz)) {
			addContract(method, arguments);
		} else {
			final TargetedCallStatement callStatement = makeCall(method, arguments);
			addCall(callStatement);
		}
	}

	@Override
	public void caseSpecialInvokeExpr(final SpecialInvokeExpr invoke) {
		caseInstanceInvokeExpr(invoke);
	}

	@Override
	public void caseNewExpr(final NewExpr newExpression) {
		final Procedure newProcedure = Prelude.instance().getNewProcedure();
		final TargetedCallStatement callStatement = newProcedure.makeTargetedCall();
		callStatement.addArgument(ValueReference.of(newExpression.getBaseType().getClassName()));
		addCall(callStatement);
	}

	@Override
	public void caseLocal(final Local local) {
		final UseDefinitionCollector definitionCollector = bodyExtractor.getDefinitionCollector();

		if (!(definitionCollector.definitionsOf(local).size() == 1) || definitionCollector.usesOf(local).size() > 1) {
			pushCastExpression(ValueReference.of(local.getName()), local);
		} else {
			super.caseLocal(local);
		}
	}

}
