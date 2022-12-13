package byteback.converter.soottoboogie.method.function;

import byteback.analysis.Namespace;
import byteback.converter.soottoboogie.ConversionException;
import byteback.converter.soottoboogie.LocalExtractor;
import byteback.converter.soottoboogie.expression.ExpressionExtractor;
import byteback.converter.soottoboogie.type.TypeAccessExtractor;
import byteback.frontend.boogie.ast.ConditionalOperation;
import byteback.frontend.boogie.ast.ExistentialQuantifier;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.OldReference;
import byteback.frontend.boogie.ast.Quantifier;
import byteback.frontend.boogie.ast.UniversalQuantifier;
import byteback.frontend.boogie.builder.QuantifierExpressionBuilder;
import byteback.frontend.boogie.builder.SetBindingBuilder;
import java.util.Iterator;
import soot.Local;
import soot.SootClass;
import soot.SootMethod;
import soot.Value;

public class FunctionExpressionExtractor extends ExpressionExtractor {

	@Override
	public FunctionExpressionExtractor makeExpressionExtractor() {
		return new FunctionExpressionExtractor();
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
		quantifierBuilder.operand(visit(argumentsIterator.next()));
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
		setExpression(new OldReference(visit(argumentsIterator.next())));
		assert !argumentsIterator.hasNext() : "Wrong number of arguments to `old` reference";
	}

	public void pushConditional(final SootMethod method, final Iterable<Value> arguments) {
		final Iterator<Value> argumentsIterator = arguments.iterator();
		final Expression condition = visit(argumentsIterator.next());
		final Expression thenExpression = visit(argumentsIterator.next());
		final Expression elseExpression = visit(argumentsIterator.next());
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