package byteback.core.converter.soottoboogie.expression;

import java.util.Set;
import soot.Local;

public class PartialSubstitutor extends Substitutor {

	public PartialSubstitutor() {
		super();
	}

	@Override
	public void handleDependencies(final Set<Local> dependencies) {
		for (Local dependency : dependencies) {
			substitutionIndex.remove(dependency);
		}
	}

}
