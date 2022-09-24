package byteback;

import soot.SootMethod;

public class BBMethod {

	private final SootMethod smethod;

	public BBMethod (final SootMethod smethod) {
		this.smethod = smethod;
	}

	public SootMethod getSootMethod() {
		return smethod;
	}
	
}
