package byteback.core.converter.soottoboogie.expression;

import byteback.core.converter.soottoboogie.ConversionException;
import byteback.frontend.boogie.ast.Expression;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import soot.Local;

public class Substitutor {

	public final Map<Local, Expression> substitutionIndex;

	public final Map<Local, Set<Local>> dependencyIndex;

	public Substitutor(final Map<Local, Expression> substitutionIndex, final Map<Local, Set<Local>> dependencyIndex) {
		this.substitutionIndex = substitutionIndex;
		this.dependencyIndex = dependencyIndex;
	}

	public Substitutor(final Map<Local, Expression> substitutions) {
		this(substitutions, new HashMap<>());
	}

	public Substitutor() {
		this(new HashMap<>(), new HashMap<>());
	}

	public Map<Local, Expression> getSubstitutionIndex() {
		return substitutionIndex;
	}

	protected void handleDependencies(final Set<Local> dependencies) {
		if (dependencies.size() > 0) {
			throw new ConversionException(
					"Dependency found, the next substituted expressions may be invalid");
		}
	}

	public final void put(final Local source, final Set<Local> targets, final Expression expression) {
		handleDependencies(dependencyIndex.getOrDefault(source, Collections.emptySet()));

		for (Local target : targets) {
			dependencyIndex.computeIfAbsent(target, ($) -> new HashSet<>()).add(source);
		}

		substitutionIndex.put(source, expression);
	}

	public Optional<Expression> substitute(final Local local) {
		return Optional.ofNullable(substitutionIndex.get(local));
	}

	public boolean substitutes(final Local local) {
		return substitutionIndex.containsKey(local);
	}

}
