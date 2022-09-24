package byteback;

import soot.SootClass;

public class BBClass {

	private final SootClass sclass;

	public BBClass(final SootClass sclass) {
		this.sclass = sclass;
	}

	public SootClass getSootClass() {
		return sclass;
	}

}
