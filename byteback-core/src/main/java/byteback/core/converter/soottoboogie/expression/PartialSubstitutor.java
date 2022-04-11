package byteback.core.converter.soottoboogie.expression;

import java.util.Set;

import byteback.frontend.boogie.ast.Expression;
import soot.Local;

public class PartialSubstitutor extends Substitutor {

  public PartialSubstitutor() {
    super();
  }

  @Override
  public void put(final Local source, final Set<Local> targets, final Expression expression) {
    super.put(source, targets, expression);
  }

}
