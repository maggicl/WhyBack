package byteback.core.converter.soottoboogie.expression;

import byteback.frontend.boogie.ast.Expression;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import soot.Local;

public class Substitutor {

	public final Map<Local, Expression> substitutions;

  public final Map<Local, Set<Local>> dependencies;

  public Substitutor(final Map<Local, Expression> substitutions, final Map<Local, Set<Local>> dependencies) {
		this.substitutions = substitutions;
    this.dependencies = dependencies;
  }

  public Substitutor(final Map<Local, Expression> substitutions) {
    this(substitutions, new HashMap<>());
  }

  public Substitutor() {
		this(new HashMap<>(), new HashMap<>());
	}

  public Map<Local, Expression> getSubstitutions() {
    return substitutions;
  }

  public void put(final Local source, final Set<Local> targets, final Expression expression) {
    for (Local dependency : dependencies.getOrDefault(source, Collections.emptySet())) {
      substitutions.remove(dependency);
    }

    for (Local target : targets) {
      dependencies.computeIfAbsent(target, ($) -> new HashSet<>()).add(source);
    }

    substitutions.put(source, expression);
  }

  public Optional<Expression> substitute(final Local local) {
		return Optional.ofNullable(substitutions.get(local));
	}

  public boolean substitutes(final Local local) {
    return substitutions.containsKey(local);
  }

}
