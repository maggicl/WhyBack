package byteback.test.operation;

import byteback.annotations.Contract.Pure;

public class VirtualCall {

	public VirtualCall() {
	}

	@Pure
	public VirtualCall getThis() {
		return this;
	}

	@Pure
	public VirtualCall getThat(VirtualCall that) {
		return that.getThis();
	}

}
