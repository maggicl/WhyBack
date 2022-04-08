package byteback.core.converter.soot.boogie;

import byteback.core.representation.soot.body.SootBody;
import byteback.core.representation.soot.body.SootExpressionVisitor;
import byteback.core.representation.soot.body.SootStatementVisitor;
import byteback.frontend.boogie.ast.AssertStatement;
import byteback.frontend.boogie.ast.AssumeStatement;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.ExtensionPoint;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import soot.Unit;
import soot.jimple.IfStmt;
import soot.jimple.toolkits.annotation.logic.Loop;

public class LoopCollector {

  public static class LoopContext {

    private final Loop loop;

    private final ExtensionPoint assertionPoint;

    private final ExtensionPoint assumptionPoint;

    public LoopContext(final Loop loop) {
      this.loop = loop;
      this.assertionPoint = new ExtensionPoint();
      this.assumptionPoint = new ExtensionPoint();
    }

    public Loop getLoop() {
      return loop;
    }

    public void addInvariant(final Expression invariant) {
      assertionPoint.addStatement(new AssertStatement(invariant));
      assumptionPoint.addStatement(new AssumeStatement(invariant));
    }

    public ExtensionPoint getAssertionPoint() {
      return assertionPoint;
    }

    public ExtensionPoint getAssumptionPoint() {
      return assumptionPoint;
    }

  }

  private final Map<Unit, LoopContext> headIndex;

  private final Map<Unit, LoopContext> backJumpIndex;

  private final Map<Unit, LoopContext> exitIndex;

  public LoopCollector() {
    this.headIndex = new HashMap<>();
    this.backJumpIndex = new HashMap<>();
    this.exitIndex = new HashMap<>();
  }

  public void collect(final SootBody body) {
    for (Loop loop : body.getLoops()) {
      final LoopContext loopContext = new LoopContext(loop);
      headIndex.put(loop.getHead(), loopContext);
      backJumpIndex.put(loop.getBackJumpStmt(), loopContext);

      for (Unit exit : loop.getLoopExits()) {
        exit.apply(new SootStatementVisitor<>() {

          @Override
          public void caseIfStmt(final IfStmt ifStatement) {
            exitIndex.put(ifStatement.getTarget(), loopContext);
          }

          @Override
          public void caseDefault(final Unit unit) {
            throw new IllegalStateException("Cannot identify exit target");
          }

        });
      }
    }
  }

  public Optional<LoopContext> getByHead(final Unit unit) {
    return Optional.ofNullable(headIndex.get(unit));
  }

  public Optional<LoopContext> getByBackJump(final Unit unit) {
    return Optional.ofNullable(backJumpIndex.get(unit));
  }

  public Optional<LoopContext> getByExit(final Unit unit) {
    return Optional.ofNullable(exitIndex.get(unit));
  }

  public Collection<LoopContext> getLoopContexts() {
    return headIndex.values();
  }

}
