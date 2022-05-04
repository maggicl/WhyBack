package byteback.core.converter.soottoboogie.method.procedure;

import byteback.core.representation.soot.body.SootBody;
import byteback.core.util.Lazy;
import java.util.Collection;
import java.util.stream.Collectors;
import soot.Local;
import soot.Unit;
import soot.toolkits.scalar.LocalDefs;
import soot.toolkits.scalar.LocalUses;
import soot.toolkits.scalar.SimpleLocalDefs;
import soot.toolkits.scalar.SimpleLocalUses;

public class UseDefinitionCollector {

	private Lazy<LocalDefs> definitions;
	private Lazy<LocalUses> uses;

	public UseDefinitionCollector() {
		definitions = Lazy.empty();
		uses = Lazy.empty();
	}

	public void collect(final SootBody body) {
		definitions = Lazy.from(() -> new SimpleLocalDefs(body.getUnitGraph()));
		uses = Lazy.from(() -> new SimpleLocalUses(body.getUnitGraph(), definitions.get()));
	}

	public boolean hasSingleDefinition(final Local local) {
		return definitionsOf(local).size() == 1;
	}

	public Collection<Unit> definitionsOfAt(final Local local, final Unit unit) {
		return definitions.get().getDefsOfAt(local, unit);
	}

	public Collection<Unit> definitionsOf(final Local local) {
		return definitions.get().getDefsOf(local).stream().collect(Collectors.toSet());
	}

	public Collection<Unit> usesOf(final Local local) {
		return definitions.get().getDefsOf(local).stream().flatMap((unit) -> {
			return uses.get().getUsesOf(unit).stream().map((pair) -> pair.unit).collect(Collectors.toSet()).stream();
		}).collect(Collectors.toSet());
	}

}