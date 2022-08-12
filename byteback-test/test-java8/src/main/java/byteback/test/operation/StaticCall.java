package byteback.test.operation;

import byteback.annotations.Contract.Pure;

public class StaticCall {

	public StaticCall() {
	}

	@Pure
	public static int getConstant() {
		return 1;
	}

	@Pure
	public static int increment(int a) {
		return a + 1;
	}

	@Pure
	public int main(StaticCall that) {
		return increment(getConstant());
	}

}
