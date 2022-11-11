package byteback.analysis;

import byteback.util.MultiMap;
import soot.Unit;
import soot.Value;

public class UseDefineChain {

	final MultiMap<Value, Unit> definitions;
	
	final MultiMap<Unit, Unit> uses;

	public UseDefineChain() {
		this.definitions = new MultiMap<>();
		this.uses = new MultiMap<>();
	}

}

