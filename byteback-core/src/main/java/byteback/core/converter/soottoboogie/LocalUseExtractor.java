package byteback.core.converter.soottoboogie;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import byteback.core.representation.soot.body.SootExpression;
import byteback.core.representation.soot.body.SootExpressionVisitor;
import soot.Local;
import soot.Value;
import soot.ValueBox;

public class LocalUseExtractor extends SootExpressionVisitor<Set<Local>> {

  final Set<Local> usedLocals;

  public LocalUseExtractor() {
    this.usedLocals = new HashSet<>();
  }

  public Set<Local> visit(final SootExpression expression) {
    final Collection<ValueBox> useBoxes = expression.getUseBoxes();

    for (ValueBox useBox : useBoxes) {
      useBox.getValue().apply(this);
    }

    return usedLocals;
  }

  @Override
  public void caseLocal(final Local local) {
    usedLocals.add(local);
  }

  @Override
  public void caseDefault(final Value value) {
  }

}
