package byteback.core.converter.soottoboogie.method.function;

import byteback.core.converter.soottoboogie.ConversionException;
import byteback.core.converter.soottoboogie.LocalExtractor;
import byteback.core.converter.soottoboogie.Namespace;
import byteback.core.converter.soottoboogie.expression.ExpressionExtractor;
import byteback.core.converter.soottoboogie.type.TypeAccessExtractor;
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
import soot.SootClass;
import soot.SootMethod;
import soot.Type;
import soot.Value;

public class FunctionExpressionExtractor extends ExpressionExtractor {

	public FunctionExpressionExtractor(final Type type) {
		super(type);
	}

	@Override
	public FunctionExpressionExtractor makeExpressionVisitor(final Type type) {
		return new FunctionExpressionExtractor(type);
	}

	public void pushQuantifier(final Quantifier quantifier, final Iterable<Value> arguments) {
		final var quantifierBuilder = new QuantifierExpressionBuilder();
		final var bindingBuilder = new SetBindingBuilder();
		final Iterator<Value> argumentsIterator = arguments.iterator();
		final Local variableLocal = new LocalExtractor().visit(argumentsIterator.next());
		bindingBuilder.typeAccess(new TypeAccessExtractor().visit(variableLocal.getType()));
		bindingBuilder.name(ExpressionExtractor.localName(variableLocal));
		quantifierBuilder.quantifier(quantifier);
		quantifierBuilder.addBinding(bindingBuilder.build());
		quantifierBuilder.operand(visit(argumentsIterator.next(), BooleanType.v()));
		setExpression(quantifierBuilder.build());
		assert !argumentsIterator.hasNext() : "Wrong number of arguments to quantifier";
	}

	public void pushExistentialQuantifier(final Iterable<Value> arguments) {
		final Quantifier quantifier = new ExistentialQuantifier();
		pushQuantifier(quantifier, arguments);
	}

	public void pushUniversalQuantifier(final Iterable<Value> arguments) {
		final Quantifier quantifier = new UniversalQuantifier();
		pushQuantifier(quantifier, arguments);
	}

	public void pushQuantifier(final SootMethod method, final Iterable<Value> arguments) {
		final String quantifierName = method.getName();

		if (quantifierName.equals(Namespace.EXISTENTIAL_QUANTIFIER_NAME)) {
			pushExistentialQuantifier(arguments);
		} else if (quantifierName.equals(Namespace.UNIVERSAL_QUANTIFIER_NAME)) {
			pushUniversalQuantifier(arguments);
		} else {
			throw new ConversionException("Unknown quantifier method: " + method.getName());
		}
	}

	public void pushOld(final SootMethod method, final Iterable<Value> arguments) {
		final Iterator<Value> argumentsIterator = arguments.iterator();
		final soot.Type argumentType = method.getParameterTypes().get(0);
		setExpression(new OldReference(visit(argumentsIterator.next(), argumentType)));
		assert !argumentsIterator.hasNext() : "Wrong number of arguments to `old` reference";
	}

	public void pushConditional(final SootMethod method, final Iterable<Value> arguments) {
		final Iterator<Value> argumentsIterator = arguments.iterator();
		final soot.Type argumentType = method.getParameterTypes().get(1);
		final Expression condition = visit(argumentsIterator.next(), BooleanType.v());
		final Expression thenExpression = visit(argumentsIterator.next(), argumentType);
		final Expression elseExpression = visit(argumentsIterator.next(), argumentType);
		setExpression(new ConditionalOperation(condition, thenExpression, elseExpression));
		assert !argumentsIterator.hasNext() : "Wrong number of arguments to conditional expression";
	}

	public void pushSpecial(final SootMethod method, final Iterable<Value> arguments) {
		final String specialName = method.getName();

		if (specialName.equals(Namespace.OLD_NAME)) {
			pushOld(method, arguments);
		} else if (specialName.equals(Namespace.CONDITIONAL_NAME)) {
			pushConditional(method, arguments);
		} else {
			throw new ConversionException("Unknown special method: " + method.getName());
		}
	}

	public void pushBinding(final SootMethod method, final Iterable<Value> arguments) {
		throw new ConversionException("Cannot bind a free variable");
	}

	@Override
	public void pushFunctionReference(final SootMethod method, final Iterable<Value> arguments) {
		final SootClass clazz = method.getDeclaringClass();
		if (Namespace.isBindingClass(clazz)) {
			pushBinding(method, arguments);
		} else if (Namespace.isQuantifierClass(clazz)) {
			pushQuantifier(method, arguments);
		} else if (Namespace.isSpecialClass(clazz)) {
			pushSpecial(method, arguments);
		} else {
			super.pushFunctionReference(method, arguments);
		}
	}

}
