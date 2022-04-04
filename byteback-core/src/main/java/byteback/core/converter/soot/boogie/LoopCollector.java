package byteback.core.converter.soot.boogie;

import byteback.core.representation.soot.body.SootBody;
import byteback.core.representation.soot.body.SootStatementVisitor;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import soot.Unit;
import soot.jimple.IfStmt;
import soot.jimple.Stmt;
import soot.jimple.toolkits.annotation.logic.Loop;

public class LoopCollector {

	private final Map<Stmt, Loop> startIndex;

  private final Map<Stmt, Loop> endIndex;

	public LoopCollector() {
		this.startIndex = new HashMap<>();
		this.endIndex = new HashMap<>();
	}

	public void collect(final SootBody body) {
    for (Loop loop : body.getLoops()) {
      if (loop.getBackJumpStmt() == loop.getHead()) {
        loop.getBackJumpStmt().apply(new SootStatementVisitor<>() {

            @Override
            public void caseIfStmt(final IfStmt ifStatement) {
              startIndex.put(ifStatement.getTarget(), loop);
            }

            @Override
            public void caseDefault(final Unit unit) {
              throw new IllegalArgumentException("Unable to determine backjump from " + unit);
            }

          });
      } else {
        startIndex.put(loop.getHead(), loop);
      }

      endIndex.put(loop.getBackJumpStmt(), loop);
    }
	}

	public Optional<Loop> getByStart(final Unit unit) {
    return Optional.ofNullable(startIndex.get(unit));
	}

	public Optional<Loop> getByEnd(final Unit unit) {
    return Optional.ofNullable(endIndex.get(unit));
	}

}
