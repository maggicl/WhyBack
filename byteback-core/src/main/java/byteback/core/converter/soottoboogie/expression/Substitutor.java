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
import soot.Value;
import soot.jimple.toolkits.infoflow.CachedEquivalentValue;

public class Substitutor {

	public final Map<Local, Expression> substitutionIndex;

	public final Map<Value, Set<Value>> dependencyIndex;

	public Substitutor(final Map<Local, Expression> substitutionIndex, final Map<Value, Set<Value>> dependencyIndex) {
		this.substitutionIndex = substitutionIndex;
		this.dependencyIndex = dependencyIndex;
	}

	public Substitutor(final Map<Local, Expression> substitutionIndex) {
		this(substitutionIndex, new HashMap<>());
	}

	public Substitutor() {
		this(new HashMap<>(), new HashMap<>());
	}

	public Map<Local, Expression> getSubstitutionIndex() {
		return substitutionIndex;
	}

	public void clear() {
		substitutionIndex.clear();
		dependencyIndex.clear();
	}

	protected void handleDependencies(final Set<Value> dependencies) {
		if (dependencies.size() > 0) {
			throw new ConversionException("Dependency found, the next substituted expressions may be invalid");
		}
	}

	public void prune(final Value dependency) {
		final var cachedDependency = new CachedEquivalentValue(dependency);
		if (dependencyIndex.containsKey(cachedDependency)) {
			for (final Value source : dependencyIndex.get(cachedDependency)) {
				substitutionIndex.remove(source);
			}
		}
	}

	public final void put(final Local source, final Set<Value> targets, final Expression expression) {
		handleDependencies(dependencyIndex.getOrDefault(source, Collections.emptySet()));

		for (Value target : targets) {
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
