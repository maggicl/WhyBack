package byteback;

import soot.SootField;

public class BBField {

	private final SootField sfield;

	public BBField(final SootField sfield) {
		this.sfield = sfield;
	}

	public SootField getSootField() {
		return sfield;
	}

}
