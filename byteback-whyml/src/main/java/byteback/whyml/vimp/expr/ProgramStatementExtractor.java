package byteback.whyml.vimp.expr;

import byteback.analysis.JimpleStmtSwitch;
import byteback.analysis.JimpleValueSwitch;
import byteback.analysis.Vimp;
import byteback.analysis.tags.PositionTag;
import byteback.analysis.vimp.AssertionStmt;
import byteback.analysis.vimp.AssumptionStmt;
import byteback.analysis.vimp.InvariantStmt;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.expr.LocalExpression;
import byteback.whyml.syntax.expr.field.Access;
import byteback.whyml.syntax.field.WhyField;
import byteback.whyml.syntax.field.WhyInstanceField;
import byteback.whyml.syntax.field.WhyStaticField;
import byteback.whyml.syntax.function.WhyFunctionSignature;
import byteback.whyml.syntax.function.WhyLocal;
import byteback.whyml.syntax.statement.ArrayAssignment;
import byteback.whyml.syntax.statement.CFGLogicalStatement;
import byteback.whyml.syntax.statement.CFGStatement;
import byteback.whyml.syntax.statement.FieldAssignment;
import byteback.whyml.syntax.statement.LocalAssignment;
import byteback.whyml.syntax.type.WhyArrayType;
import byteback.whyml.syntax.type.WhyJVMType;
import byteback.whyml.syntax.type.WhyType;
import byteback.whyml.vimp.TypeResolver;
import byteback.whyml.vimp.VimpFieldParser;
import byteback.whyml.vimp.VimpLocalParser;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.grimp.Grimp;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.CaughtExceptionRef;
import soot.jimple.IdentityStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InvokeStmt;
import soot.jimple.ParameterRef;
import soot.jimple.StaticFieldRef;
import soot.jimple.ThisRef;
import soot.tagkit.AbstractHost;

public class ProgramStatementExtractor extends JimpleStmtSwitch<List<CFGStatement>> {
	private final ProgramExpressionExtractor programExpressionExtractor;
	private final PureExpressionExtractor pureExpressionExtractor;
	private final VimpLocalParser vimpLocalParser;
	private final VimpFieldParser fieldParser;
	private final TypeResolver typeResolver;

	private final WhyFunctionSignature signature;
	private final List<CFGStatement> statements = new ArrayList<>();

	public ProgramStatementExtractor(ProgramExpressionExtractor programExpressionExtractor,
									 PureExpressionExtractor pureExpressionExtractor,
									 VimpLocalParser vimpLocalParser,
									 VimpFieldParser fieldParser,
									 TypeResolver typeResolver,
									 WhyFunctionSignature signature) {
		this.programExpressionExtractor = programExpressionExtractor;
		this.pureExpressionExtractor = pureExpressionExtractor;
		this.vimpLocalParser = vimpLocalParser;
		this.fieldParser = fieldParser;
		this.typeResolver = typeResolver;
		this.signature = signature;
	}

	@Override
	public List<CFGStatement> result() {
		return statements;
	}

	private void addStatement(CFGStatement statement) {
		statements.add(statement);
	}

	@Override
	public void caseIdentityStmt(final IdentityStmt identity) {
		final Value lValue = identity.getLeftOp();
		final Value rValue = identity.getRightOp();

		if (lValue instanceof Local local) {
			if (rValue instanceof CaughtExceptionRef) {
				visit(Grimp.v().newAssignStmt(local, Vimp.v().newCaughtExceptionRef()));
			} else if (rValue instanceof ParameterRef || rValue instanceof ThisRef) {
				final WhyLocal lValueLocal = vimpLocalParser.parse(local);

				final WhyLocal rValueLocal;
				if (rValue instanceof ParameterRef paramRef) {
					rValueLocal = signature.getParam(paramRef.getIndex()).orElseThrow(() ->
							new WhyTranslationException(identity, "parameter " + paramRef + " missing from signature: " + signature));
				} else {
					rValueLocal = signature.getThisParam().orElseThrow(() ->
							new WhyTranslationException(identity, "'this' parameter missing from signature: " + signature));
				}

				addStatement(new LocalAssignment(
						lValueLocal,
						new LocalExpression(rValueLocal.name(), rValueLocal.type().jvm())
				));
			}
		}
	}

	@Override
	public void caseAssignStmt(final AssignStmt assignment) {
		final Value left = assignment.getLeftOp();
		final Value right = assignment.getRightOp();
		final Expression rValue = programExpressionExtractor.visit(right);

		left.apply(new JimpleValueSwitch<>() {
			@Override
			public void caseLocal(final Local local) {
				addStatement(new LocalAssignment(vimpLocalParser.parse(local), rValue));
			}

			@Override
			public void caseInstanceFieldRef(final InstanceFieldRef v) {
				final WhyField field = fieldParser.parse(v.getField());
				if (!(field instanceof WhyInstanceField)) {
					throw new WhyTranslationException(v, "InstanceFieldRef has a non-instance field: " + field);
				}

				final Expression base = programExpressionExtractor.visit(v.getBase());
				addStatement(new FieldAssignment(Access.instance(base, (WhyInstanceField) field), rValue));
			}

			@Override
			public void caseStaticFieldRef(final StaticFieldRef v) {
				final WhyField field = fieldParser.parse(v.getField());
				if (!(field instanceof WhyStaticField)) {
					throw new WhyTranslationException(v, "StaticFieldRef has a non-static field: " + field);
				}

				addStatement(new FieldAssignment(Access.staticAccess((WhyStaticField) field), rValue));
			}

			@Override
			public void caseArrayRef(final ArrayRef v) {
				final WhyType type = typeResolver.resolveType(v.getBase().getType());
				if (!(type instanceof WhyArrayType)) {
					throw new WhyTranslationException(v, "ArrayRef has field with non-array type: " + type);
				}

				final WhyJVMType elemType = ((WhyArrayType) type).baseType().jvm();
				final Expression base = programExpressionExtractor.visit(v.getBase());
				final Expression index = programExpressionExtractor.visit(v.getIndex());

				addStatement(new ArrayAssignment(base, elemType, index, rValue));
			}

			@Override
			public void caseCaughtExceptionRef(final CaughtExceptionRef exceptionReference) {
				addStatement(new LocalAssignment(WhyLocal.CAUGHT_EXCEPTION, rValue));
			}

			@Override
			public void caseDefault(final Value value) {
				throw new WhyTranslationException(value, "Unknown Soot class in assignment r-value: " + value.getClass().getName());
			}
		});
	}

	@Override
	public void caseInvokeStmt(final InvokeStmt invokeStatement) {
		final var invoke = invokeStatement.getInvokeExpr();
		programExpressionExtractor.visit(invoke);
	}

	private Optional<String> positionAttribute(AbstractHost stmt, String string) {
		if (stmt.hasTag("PositionTag")) {
			final PositionTag tag = (PositionTag) stmt.getTag("PositionTag");
			return Optional.of("%s: (line %d): %s might not hold".formatted(tag.file, tag.lineNumber, string));
		} else {
			return Optional.empty();
		}
	}

	@Override
	public void caseAssertionStmt(final AssertionStmt assertionStmt) {
		final Expression condition = pureExpressionExtractor.visit(assertionStmt.getCondition());
		final Optional<String> position = positionAttribute(assertionStmt, "assertion");

		addStatement(new CFGLogicalStatement(CFGLogicalStatement.Kind.ASSERT, position, condition));
	}

	@Override
	public void caseAssumptionStmt(final AssumptionStmt assumptionStmt) {
		final Expression condition = pureExpressionExtractor.visit(assumptionStmt.getCondition());
		final Optional<String> position = positionAttribute(assumptionStmt, "assumption");

		addStatement(new CFGLogicalStatement(CFGLogicalStatement.Kind.ASSUME, position, condition));
	}

	@Override
	public void caseInvariantStmt(final InvariantStmt assumptionStmt) {
		final Expression condition = pureExpressionExtractor.visit(assumptionStmt.getCondition());
		final Optional<String> position = positionAttribute(assumptionStmt, "invariant");

		addStatement(new CFGLogicalStatement(CFGLogicalStatement.Kind.INVARIANT, position, condition));
	}

	// TODO: variants

	@Override
	public void caseDefault(final Unit unit) {
		throw new WhyTranslationException(unit, "Unknown Soot statement: " + unit.getClass().getName());
	}
}
