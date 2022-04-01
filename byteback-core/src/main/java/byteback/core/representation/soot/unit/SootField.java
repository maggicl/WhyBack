package byteback.core.representation.soot.unit;

import byteback.core.representation.soot.type.SootType;

public class SootField {

	private final soot.SootField sootField;

	public SootField(final soot.SootField sootField) {
		this.sootField = sootField;
	}

	public String getName() {
		return sootField.getName();
	}

	public SootClass getSootClass() {
		return new SootClass(sootField.getDeclaringClass());
	}

	public SootType getType() {
		return new SootType(sootField.getType());
	}

}
