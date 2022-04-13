package byteback.core.converter.soottoboogie.function;

import byteback.core.converter.soottoboogie.LocalUseExtractor;
import byteback.core.converter.soottoboogie.expression.SubstitutingExtractor;
import byteback.core.converter.soottoboogie.expression.Substitutor;
import byteback.core.representation.soot.body.SootExpression;
import byteback.core.representation.soot.body.SootExpressionVisitor;
import byteback.core.representation.soot.body.SootStatementVisitor;
import byteback.core.representation.soot.type.SootType;
import byteback.core.util.CountingMap;
import byteback.frontend.boogie.ast.Expression;

import java.util.Map.Entry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import soot.*;
import soot.jimple.*;

public class FunctionBodyExtractor extends SootStatementVisitor<Expression> {

  public static class LocalExtractor extends SootExpressionVisitor<Local> {

    private Local local;

    @Override
    public void caseLocal(final Local local) {
      this.local = local;
    }

    @Override
    public void caseDefault(final Value expression) {
      throw new IllegalArgumentException("Expected local definition, got " + expression);
    }

    @Override
    public Local result() {
      if (local == null) {
        throw new IllegalStateException("Could not retrieve local reference");
      } else {
        return local;
      }
    }

  }

  private static final Logger log = LoggerFactory.getLogger(FunctionBodyExtractor.class);

  private final CountingMap<Local, Expression> expressionTable;

  private final Substitutor substitutor;

  private final SootType returnType;

  private Expression result;

  public FunctionBodyExtractor(final SootType returnType) {
    this.returnType = returnType;
    this.expressionTable = new CountingMap<>();
    this.substitutor = new Substitutor(this.expressionTable);
  }

  @Override
  public void caseAssignStmt(final AssignStmt assignment) {
    final var left = new SootExpression(assignment.getLeftOp());
    final var right = new SootExpression(assignment.getRightOp());
    final Local local = new LocalExtractor().visit(left);
    final Expression boogieExpression = new SubstitutingExtractor(substitutor).visit(right, left.getType());
    substitutor.put(local, new LocalUseExtractor().visit(right), boogieExpression);
  }

  @Override
  public void caseReturnStmt(final ReturnStmt returnStatement) {
    final var operand = new SootExpression(returnStatement.getOp());
    result = new SubstitutingExtractor(substitutor).visit(operand, returnType);

    for (Entry<Local, Integer> entry : expressionTable.getAccessCount().entrySet()) {
      if (entry.getValue() == 0) {
        log.warn("Local assignment {} unused in final expansion", entry.getKey());
      }
    }
  }

  @Override
  public void caseDefault(final Unit unit) {
    throw new UnsupportedOperationException("Cannot substitute statement " + unit);
  }

  @Override
  public Expression result() {
    return result;
  }

}