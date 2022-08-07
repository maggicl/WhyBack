package byteback.core.preprocessing;

import byteback.core.util.MultiMap;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.jimple.toolkits.infoflow.CachedEquivalentValue;
import soot.util.Chain;

public class DependencyCollector {

	final MultiMap<Value, Unit> from;

	final MultiMap<Unit, Value> to;

	public DependencyCollector() {
		this.from = new MultiMap<>();
		this.to = new MultiMap<>();
	}

	public void collect(final Unit unit) {
		Local local = null;
	}

}
