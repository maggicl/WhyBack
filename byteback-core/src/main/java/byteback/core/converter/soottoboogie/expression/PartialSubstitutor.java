package byteback.core.converter.soottoboogie.expression;

import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.ValueReference;
import soot.Local;

public class PartialSubstitutor extends Substitutor {

  public PartialSubstitutor() {
    super();
  }

  @Override
  public void put(final Local local, final Expression expression) {
    if (!getSubstitutions().containsKey(local)) {
      super.put(local, expression);
    } else {
      super.put(local, ValueReference.of(local.getName()));
    }
  }

}
