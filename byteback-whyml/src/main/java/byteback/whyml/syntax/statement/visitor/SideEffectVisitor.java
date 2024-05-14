package byteback.whyml.syntax.statement.visitor;

import byteback.whyml.identifiers.Identifier;
import byteback.whyml.syntax.expr.FunctionCall;
import byteback.whyml.syntax.expr.NewArrayExpression;
import byteback.whyml.syntax.expr.NewExpression;
import byteback.whyml.syntax.expr.StringLiteralExpression;
import byteback.whyml.syntax.expr.field.Access;
import byteback.whyml.syntax.function.WhyFunctionSignature;
import byteback.whyml.syntax.function.WhyLocal;
import byteback.whyml.syntax.function.WhySideEffects;
import byteback.whyml.syntax.statement.ArrayAssignment;
import byteback.whyml.syntax.statement.CFGTerminator;
import byteback.whyml.syntax.statement.FieldAssignment;
import byteback.whyml.syntax.statement.LocalAssignment;
import java.util.HashSet;
import java.util.Set;

public class SideEffectVisitor extends StatementVisitor {
	private final Set<String> reads = new HashSet<>();
	private final Set<String> writes = new HashSet<>();
	private final Set<WhyFunctionSignature> calls = new HashSet<>();

	@Override
	public void visitNewArrayExpression(NewArrayExpression newArrayExpression) {
		writes.add("%s.ptrs".formatted(Identifier.Special.HEAP));
		writes.add("%s.arr".formatted(Identifier.Special.HEAP));
		writes.add("%s.typeof".formatted(Identifier.Special.HEAP));

		super.visitNewArrayExpression(newArrayExpression);
	}

	@Override
	public void visitNewExpression(NewExpression newExpression) {
		writes.add("%s.ptrs".formatted(Identifier.Special.HEAP));
		writes.add("%s.typeof".formatted(Identifier.Special.HEAP));

		super.visitNewExpression(newExpression);
	}

	@Override
	public void visitArrayAssignmentStatement(ArrayAssignment arrayAssignment) {
		writes.add("%s.arr".formatted(Identifier.Special.HEAP));

		super.visitArrayAssignmentStatement(arrayAssignment);
	}

	@Override
	public void visitFieldAssignmentStatement(FieldAssignment fieldAssignment) {
		final boolean isStatic = fieldAssignment.access() instanceof Access.Static;
		writes.add("%s.%s".formatted(
				Identifier.Special.HEAP,
				isStatic ? "staticptr" : "instptr"
		));

		super.visitFieldAssignmentStatement(fieldAssignment);
	}

	@Override
	public void visitThrowStatement(CFGTerminator.Throw aThrow) {
		writes.add(Identifier.Special.CAUGHT_EXCEPTION.toString());

		super.visitThrowStatement(aThrow);
	}

	@Override
	public void visitLocalAssignmentStatement(LocalAssignment localAssignment) {
		if (localAssignment.lValue() == WhyLocal.CAUGHT_EXCEPTION) {
			writes.add(Identifier.Special.CAUGHT_EXCEPTION.toString());
		}

		super.visitLocalAssignmentStatement(localAssignment);
	}

	@Override
	public void visitFunctionCall(FunctionCall source) {
		calls.add(source.signature());

		super.visitFunctionCall(source);
	}

	@Override
	public void visitStringLiteralExpression(StringLiteralExpression source) {
		reads.add("Java.Lang.String.pointers'8");

		super.visitStringLiteralExpression(source);
	}

	public WhySideEffects sideEffects() {
		return new WhySideEffects(reads, writes, calls);
	}
}
