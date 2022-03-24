package byteback.dummy.function;

import byteback.annotations.Contract.Pure;

public class Virtual {

	@Pure
	public Virtual getThis() {
		return this;
	}

	@Pure
	public Virtual getThat(Virtual that) {
		return that.getThis();
	}

}
