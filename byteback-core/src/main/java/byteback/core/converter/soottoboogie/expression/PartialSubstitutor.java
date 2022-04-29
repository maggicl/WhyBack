package byteback.core.converter.soottoboogie.expression;

import java.util.Set;

import soot.Value;

public class PartialSubstitutor extends Substitutor {

	public PartialSubstitutor() {
		super();
	}

	@Override
	public void handleDependencies(final Set<Value> dependencies) {
		for (Value dependency : dependencies) {
			substitutionIndex.remove(dependency);
		}
	}

}
