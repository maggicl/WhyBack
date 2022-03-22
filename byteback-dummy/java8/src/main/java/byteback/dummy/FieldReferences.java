package byteback.dummy;

import byteback.annotations.Contract.Pure;

public class FieldReferences {

	public int field;

	@Pure
	public int fieldReference() {
		return this.field;
	}

	@Pure
	public int fieldSum() {
		return this.field + 2;
	}

}
