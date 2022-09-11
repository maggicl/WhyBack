package byteback.core.converter.soottoboogie.method.procedure;

import byteback.core.representation.soot.body.SootBodies;
import byteback.core.util.Lazy;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import soot.Body;
import soot.Local;
import soot.Unit;
import soot.ValueBox;
import soot.toolkits.scalar.LocalDefs;
import soot.toolkits.scalar.LocalUses;
import soot.toolkits.scalar.SimpleLocalDefs;
import soot.toolkits.scalar.SimpleLocalUses;
import soot.toolkits.scalar.UnitValueBoxPair;

public class DefinitionsCollector {

	private Lazy<LocalDefs> definitions;

	private Lazy<LocalUses> uses;

	public DefinitionsCollector() {
		definitions = Lazy.empty();
		uses = Lazy.empty();
	}

	public void collect(final Body body) {
		definitions = Lazy.from(() -> new SimpleLocalDefs(SootBodies.getUnitGraph(body)));
		uses = Lazy.from(() -> new SimpleLocalUses(SootBodies.getUnitGraph(body), definitions.get()));
	}

	public boolean hasSingleDefinition(final Local local) {
		return definitionsOf(local).size() == 1;
	}

	public boolean hasSingleUse(final Local local) {
		return valueUsesOf(local).size() == 1;
	}

	public List<Unit> definitionsOfAt(final Local local, final Unit unit) {
		return definitions.get().getDefsOfAt(local, unit);
	}

	public Set<Unit> definitionsOf(final Local local) {
		return definitions.get().getDefsOf(local).stream().collect(Collectors.toSet());
	}

	public Stream<UnitValueBoxPair> usesOf(final Local local) {
		return definitions.get().getDefsOf(local).stream().flatMap((unit) -> uses.get().getUsesOf(unit).stream());
	}

	public Set<Unit> unitUsesOf(final Local local) {
		return usesOf(local).map((pair) -> pair.getUnit()).collect(Collectors.toSet());
	}

	public Set<ValueBox> valueUsesOf(final Local local) {
		return usesOf(local).map((pair) -> pair.getValueBox()).collect(Collectors.toSet());
	}

}
