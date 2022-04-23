package byteback.core.converter.soottoboogie.method.function;

import byteback.core.converter.soottoboogie.Annotations;
import byteback.core.converter.soottoboogie.ConversionException;
import byteback.core.converter.soottoboogie.LocalExtractor;
import byteback.core.converter.soottoboogie.expression.SubstitutingExtractor;
import byteback.core.converter.soottoboogie.expression.Substitutor;
import byteback.core.converter.soottoboogie.type.TypeAccessExtractor;
import byteback.core.representation.soot.body.SootExpression;
import byteback.core.representation.soot.type.SootType;
import byteback.core.representation.soot.unit.SootMethod;
import byteback.frontend.boogie.ast.ConditionalOperation;
import byteback.frontend.boogie.ast.ExistentialQuantifier;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.OldReference;
import byteback.frontend.boogie.ast.Quantifier;
import byteback.frontend.boogie.ast.UniversalQuantifier;
import byteback.frontend.boogie.builder.QuantifierExpressionBuilder;
import byteback.frontend.boogie.builder.SetBindingBuilder;
import java.util.Iterator;
import soot.BooleanType;
import soot.Local;

public class FunctionExpressionExtractor extends SubstitutingExtractor {

	public FunctionExpressionExtractor(final Substitutor substitutor) {
		super(substitutor);
	}

	public void pushQuantifier(final Quantifier quantifier, final Iterable<SootExpression> arguments) {
		final var quantifierBuilder = new QuantifierExpressionBuilder();
		final var bindingBuilder = new SetBindingBuilder();
		final Iterator<SootExpression> argumentsIterator = arguments.iterator();
		final Local variableLocal = new LocalExtractor().visit(argumentsIterator.next());
		bindingBuilder.typeAccess(new TypeAccessExtractor().visit(new SootType(variableLocal.getType())));
		bindingBuilder.name(variableLocal.getName());
		quantifierBuilder.quantifier(quantifier);
		quantifierBuilder.addBinding(bindingBuilder.build());
		quantifierBuilder.operand(visit(argumentsIterator.next(), new SootType(BooleanType.v())));
		pushExpression(quantifierBuilder.build());
		assert !argumentsIterator.hasNext() : "Wrong number of arguments to quantifier";
	}

	public void pushExistentialQuantifier(Iterable<SootExpression> arguments) {
    final Quantifier quantifier = new ExistentialQuantifier();
    pushQuantifier(quantifier, arguments);
	}

	public void pushUniversalQuantifier(final Iterable<SootExpression> arguments) {
    final Quantifier quantifier = new UniversalQuantifier();
    pushQuantifier(quantifier, arguments);
	}

	public void pushQuantifier(final SootMethod method, final Iterable<SootExpression> arguments) {
		final String quantifierName = method.getName();

		if (quantifierName.equals(Annotations.EXISTENTIAL_QUANTIFIER_NAME)) {
			pushExistentialQuantifier(arguments);
		} else if (quantifierName.equals(Annotations.UNIVERSAL_QUANTIFIER_NAME)) {
			pushUniversalQuantifier(arguments);
		} else {
			throw new ConversionException("Unknown quantifier method: " + method.getName());
		}
	}

	public void pushOld(final SootMethod method, final Iterable<SootExpression> arguments) {
		final Iterator<SootExpression> argumentsIterator = arguments.iterator();
		final SootType argumentType = method.getParameterTypes().get(0);
		pushExpression(new OldReference(visit(argumentsIterator.next(), argumentType)));
		assert !argumentsIterator.hasNext() : "Wrong number of arguments to `old` reference";
	}

	public void pushConditional(final SootMethod method, final Iterable<SootExpression> arguments) {
		final Iterator<SootExpression> argumentsIterator = arguments.iterator();
		final SootType argumentType = method.getParameterTypes().get(1);
		final Expression condition = visit(argumentsIterator.next(), new SootType(BooleanType.v()));
		final Expression thenExpression = visit(argumentsIterator.next(), argumentType);
		final Expression elseExpression = visit(argumentsIterator.next(), argumentType);
		pushExpression(new ConditionalOperation(condition, thenExpression, elseExpression));
		assert !argumentsIterator.hasNext() : "Wrong number of arguments to conditional expression";
	}

	public void pushSpecial(final SootMethod method, final Iterable<SootExpression> arguments) {
		final String specialName = method.getName();

		if (specialName.equals(Annotations.OLD_NAME)) {
			pushOld(method, arguments);
		} else if (specialName.equals(Annotations.CONDITIONAL_NAME)) {
			pushConditional(method, arguments);
		} else {
			throw new ConversionException("Unknown special method: " + method.getName());
		}
	}

	public void pushBinding(final SootMethod method, final Iterable<SootExpression> arguments) {
		throw new ConversionException("Cannot bind a free variable");
	}

	@Override
	public void pushFunctionReference(final SootMethod method, final Iterable<SootExpression> arguments) {
		if (method.getSootClass().equals(Annotations.BINDING_CLASS.get())) {
			pushBinding(method, arguments);
		} else if (method.getSootClass().equals(Annotations.QUANTIFIER_CLASS.get())) {
			pushQuantifier(method, arguments);
		} else if (method.getSootClass().equals(Annotations.SPECIAL_CLASS.get())) {
			pushSpecial(method, arguments);
		} else {
			super.pushFunctionReference(method, arguments);
		}
	}

}
