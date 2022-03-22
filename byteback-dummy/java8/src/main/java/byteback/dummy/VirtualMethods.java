package byteback.dummy;

import byteback.annotations.Contract.Pure;

public class VirtualMethods {

	@Pure
	public VirtualMethods getThis() {
		return this;
	}

	@Pure
	public VirtualMethods getThat(VirtualMethods that) {
		return that.getThis();
	}

}
