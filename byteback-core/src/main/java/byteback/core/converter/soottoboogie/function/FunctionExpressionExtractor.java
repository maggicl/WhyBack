package byteback.core.converter.soottoboogie.function;

import byteback.core.converter.soottoboogie.Annotations;
import byteback.core.converter.soottoboogie.LocalExtractor;
import byteback.core.converter.soottoboogie.expression.SubstitutingExtractor;
import byteback.core.converter.soottoboogie.expression.Substitutor;
import byteback.core.converter.soottoboogie.type.TypeAccessExtractor;
import byteback.core.representation.soot.body.SootExpression;
import byteback.core.representation.soot.type.SootType;
import byteback.core.representation.soot.unit.SootMethod;
import byteback.frontend.boogie.ast.ExistentialQuantifier;
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
	}

	public void pushExistentialQuantifier(Iterable<SootExpression> arguments) {
		final Quantifier quantifier = ExistentialQuantifier.instance();
		pushQuantifier(quantifier, arguments);
	}

	public void pushUniversalQuantifier(final Iterable<SootExpression> arguments) {
		final Quantifier quantifier = UniversalQuantifier.instance();
		pushQuantifier(quantifier, arguments);
	}

	public void pushQuantifier(final SootMethod method, final Iterable<SootExpression> arguments) {
		final String quantifierName = method.getName();

		if (quantifierName.equals(Annotations.EXISTENTIAL_QUANTIFIER_NAME)) {
			pushExistentialQuantifier(arguments);
		} else if (quantifierName.equals(Annotations.UNIVERSAL_QUANTIFIER_NAME)) {
			pushUniversalQuantifier(arguments);
		} else {
			throw new RuntimeException("Unknown quantifier method: " + method.getName());
		}
	}

	@Override
	public void pushFunctionReference(final SootMethod method, final Iterable<SootExpression> arguments) {
		if (method.getSootClass().equals(Annotations.QUANTIFIER_CLASS.get())) {
			pushQuantifier(method, arguments);
		} else {
			super.pushFunctionReference(method, arguments);
		}
	}

}
