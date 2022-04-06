package byteback.core.converter.soot.boogie;

import byteback.core.representation.soot.body.SootBody;
import java.util.Collection;
import soot.Local;
import soot.Unit;
import soot.toolkits.scalar.LocalDefs;
import soot.toolkits.scalar.SimpleLocalDefs;

public class DefinitionCollector {

	private LocalDefs analysis;

	public void collect(final SootBody body) {
		analysis = new SimpleLocalDefs(body.getUnitGraph());
	}

	public Collection<Unit> definitionsOfAt(final Local local, final Unit unit) {
		return analysis.getDefsOfAt(local, unit);
	}

	public Collection<Unit> definitionsOf(final Local local) {
		return analysis.getDefsOf(local);
	}

}
