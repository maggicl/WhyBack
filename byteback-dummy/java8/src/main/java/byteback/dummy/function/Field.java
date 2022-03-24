package byteback.dummy.function;

import byteback.annotations.Contract.Pure;

public class Field {

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
