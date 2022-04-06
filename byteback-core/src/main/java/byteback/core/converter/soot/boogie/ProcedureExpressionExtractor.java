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
import soot.BooleanType;

import java.util.Iterator;
import java.util.Optional;
import java.util.function.Supplier;

public class ProcedureExpressionExtractor extends ExpressionExtractor {

	public static interface VariableSupplier extends Supplier<ValueReference> {
	};

	public static TargetedCallStatement makeCall(final SootMethod method, final List<Expression> arguments) {
		final String methodName = NameConverter.methodName(method);
		final TargetedCallStatement call = new TargetedCallStatement();
		call.setAccessor(new Accessor(methodName));
		call.setArgumentList(arguments);

		return call;
	}

	final StatementExtractor extractor;

	final VariableSupplier supplier;

	public ProcedureExpressionExtractor(final StatementExtractor extractor, final VariableSupplier supplier) {
		this.extractor = extractor;
		this.supplier = supplier;
	}

	public void addCall(final SootMethod method, final Iterable<SootExpression> arguments) {
    final List<Expression> boogieArguments = convertArguments(method, arguments);
    final TargetedCallStatement callStatement = makeCall(method, boogieArguments);
		final List<SymbolicReference> targets = new List<SymbolicReference>();

		if (supplier != null) {
			final ValueReference reference = supplier.get();
			targets.add(reference);
			pushExpression(reference);
		}

		callStatement.setTargetList(targets);
		extractor.addStatement(callStatement);
	}

	public void addSpecial(final SootMethod method, final Iterable<SootExpression> arguments) {
    final Iterator<SootExpression> argumentIterator = arguments.iterator();
    final SootExpression argument = argumentIterator.next();
    final Expression boogieArgument = new InlineExpressionExtractor(extractor.getExpressionTable()).visit(argument, new SootType(BooleanType.v()));
    assert !argumentIterator.hasNext();

    if (method.equals(Annotations.ASSERT_METHOD)) {
			extractor.addStatement(new AssertStatement(boogieArgument));
		} else if (method.equals(Annotations.ASSUME_METHOD)) {
			extractor.addStatement(new AssumeStatement(boogieArgument));
		} else if (method.equals(Annotations.INVARIANT_METHOD)) {
      extractor.addInvariant(boogieArgument);
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
