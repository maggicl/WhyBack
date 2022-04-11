package byteback.core.converter.soottoboogie.expression;

import byteback.frontend.boogie.ast.Expression;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import soot.Local;

public class Substitutor {

	public final Map<Local, Expression> substitutions;

	public Substitutor(final Map<Local, Expression> substitutions) {
		this.substitutions = substitutions;
	}

	public Substitutor() {
		this(new HashMap<>());
	}

  public Map<Local, Expression> getSubstitutions() {
    return substitutions;
  }

  public void put(final Local local, final Expression expression) {
		substitutions.put(local, expression);
	}

	public Optional<Expression> substitute(final Local local) {
		return Optional.ofNullable(substitutions.get(local));
	}

  public boolean substitutes(final Local local) {
    return substitutions.containsKey(local);
  }

}
