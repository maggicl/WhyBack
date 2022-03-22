package byteback.core.representation.soot.unit;

import byteback.core.representation.soot.type.SootType;
import soot.SootField;

public class SootFieldUnit {

	private final SootField sootField;

	public SootFieldUnit(final SootField sootField) {
		this.sootField = sootField;
	}

	public String getName() {
		return sootField.getName();
	}

	public SootClassUnit getClassUnit() {
		return new SootClassUnit(sootField.getDeclaringClass());
	}

	public SootType getType() {
		return new SootType(sootField.getType());
	}

}
