package byteback.core.converter.soot.boogie;

import byteback.core.representation.soot.annotation.SootAnnotation;
import byteback.core.representation.soot.unit.SootMethod;
import byteback.frontend.boogie.ast.Accessor;
import byteback.frontend.boogie.ast.AssertStatement;
import byteback.frontend.boogie.ast.AssumeStatement;
import byteback.frontend.boogie.ast.Body;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.List;
import byteback.frontend.boogie.ast.SymbolicReference;
import byteback.frontend.boogie.ast.TargetedCallStatement;
import byteback.frontend.boogie.ast.ValueReference;
import java.util.Optional;

public class ProcedureExpressionExtractor extends ExpressionExtractor {

	public static TargetedCallStatement makeCall(final SootMethod method, final List<Expression> arguments) {
		final String methodName = NameConverter.methodName(method);
		final TargetedCallStatement call = new TargetedCallStatement();
		call.setAccessor(new Accessor(methodName));
		call.setArgumentList(arguments);

		return call;
	}

	final Body body;

	final ValueReference reference;

	public ProcedureExpressionExtractor(final Body body, final ValueReference reference) {
		this.body = body;
		this.reference = reference;
	}

  public ProcedureExpressionExtractor(final Body body) {
    this(body, null);
  }

	public void addCall(final SootMethod method, final List<Expression> arguments) {
		final TargetedCallStatement callStatement = makeCall(method, arguments);
    final List<SymbolicReference> targets = new List<SymbolicReference>();

    if (reference != null) {
      targets.add(reference);
      pushExpression(reference);
    }

    callStatement.setTargetList(targets);
		body.addStatement(callStatement);
  }

  public void addSpecial(final SootMethod method, final List<Expression> arguments) {
    if (method.equals(Annotations.ASSERT_METHOD)) {
      body.addStatement(new AssertStatement(arguments.getChild(0)));
    } else if (method.equals(Annotations.ASSUME_METHOD)) {
      body.addStatement(new AssumeStatement(arguments.getChild(0)));
    } else {
      throw new RuntimeException("Unknown special method: " + method.getName());
    }
  }

	@Override
	public void pushFunctionReference(final SootMethod method, final List<Expression> arguments) {
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
