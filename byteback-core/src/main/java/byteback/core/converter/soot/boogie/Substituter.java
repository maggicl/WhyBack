package byteback.core.converter.soot.boogie;

import byteback.frontend.boogie.ast.Expression;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import soot.Local;

public class Substituter {

	public final Map<Local, Expression> substitutions;

	public Substituter(final Map<Local, Expression> substitutions) {
		this.substitutions = substitutions;
	}

	public Substituter() {
		this(new HashMap<>());
	}

	public void put(final Local local, final Expression expression) {
		substitutions.put(local, expression);
	}

	public Optional<Expression> substitute(final Local local) {
		return Optional.ofNullable(substitutions.get(local));
	}

}