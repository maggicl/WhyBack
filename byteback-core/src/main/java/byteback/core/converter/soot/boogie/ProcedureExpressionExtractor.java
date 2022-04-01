package byteback.core.converter.soot.boogie;

import byteback.core.representation.soot.annotation.SootAnnotation;
import byteback.core.representation.soot.type.SootType;
import byteback.core.representation.soot.unit.SootMethod;
import byteback.frontend.boogie.ast.Accessor;
import byteback.frontend.boogie.ast.Body;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.List;
import byteback.frontend.boogie.ast.SymbolicReference;
import byteback.frontend.boogie.ast.TargetedCallStatement;
import byteback.frontend.boogie.ast.TypeAccess;
import byteback.frontend.boogie.ast.ValueReference;
import byteback.frontend.boogie.ast.VariableDeclaration;
import java.util.Optional;
import soot.jimple.InvokeExpr;

public class ProcedureExpressionExtractor extends ExpressionExtractor {

	public static TargetedCallStatement makeCall(final InvokeExpr invoke, final List<Expression> arguments) {
		final SootMethod method = new SootMethod(invoke.getMethod());
		final String methodName = NameConverter.methodName(method);
		final TargetedCallStatement call = new TargetedCallStatement();
		call.setAccessor(new Accessor(methodName));
		call.setArgumentList(arguments);

		return call;
	}

	final Body body;

	public ProcedureExpressionExtractor(final SootType type, final Body body, final int seed) {
		super(type);
		this.body = body;
	}

	public void pushCallResult(final InvokeExpr invoke, final List<Expression> arguments) {
		final SootType type = new SootType(invoke.getType());
		final TypeAccess typeAccess = new TypeAccessExtractor().visit(type);
		final VariableDeclaration variableDeclaration = Prelude.generateVariableDeclaration(seed, typeAccess);
		final ValueReference resultReference = Prelude.generateVariableReference(seed);
		final TargetedCallStatement callStatement = makeCall(invoke, arguments);
		body.addLocalDeclaration(variableDeclaration);
		callStatement.setTargetList(new List<SymbolicReference>(resultReference));
		body.addStatement(callStatement);
		pushExpression(resultReference);
	}

	public void pushSpecial(final InvokeExpr invoke, final List<Expression> arguments) {
	}

	@Override
	public void pushFunctionReference(final InvokeExpr invoke, final List<Expression> arguments) {
		final SootMethod method = new SootMethod(invoke.getMethod());
		final Optional<SootAnnotation> annotation = method.getAnnotation("Lbyteback/annotations/Contract$Prelude;");

		if (annotation.isPresent()) {
			super.pushFunctionReference(invoke, arguments);
		} else {
			pushCallResult(invoke, arguments);
		}
	}

}
