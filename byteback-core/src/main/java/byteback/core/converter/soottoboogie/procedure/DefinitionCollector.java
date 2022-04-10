package byteback.core.converter.soottoboogie.procedure;

import byteback.core.representation.soot.body.SootBody;
import byteback.core.util.Lazy;
import java.util.Collection;
import soot.Local;
import soot.Unit;
import soot.toolkits.scalar.LocalDefs;
import soot.toolkits.scalar.SimpleLocalDefs;

public class DefinitionCollector {

	private Lazy<LocalDefs> definitions;

	public DefinitionCollector() {
		definitions = Lazy.empty();
	}

	public void collect(final SootBody body) {
		definitions = Lazy.from(() -> new SimpleLocalDefs(body.getUnitGraph()));
	}

	public boolean hasSingleDefinition(final Local local, final Unit unit) {
		return definitions.get().getDefsOfAt(local, unit).size() == 1;
	}

	public Collection<Unit> definitionsOfAt(final Local local, final Unit unit) {
		return definitions.get().getDefsOfAt(local, unit);
	}

	public Collection<Unit> definitionsOf(final Local local) {
		return definitions.get().getDefsOf(local);
	}

}
