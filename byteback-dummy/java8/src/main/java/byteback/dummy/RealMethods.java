package byteback.dummy;

import byteback.annotations.Contract.Pure;

public class RealMethods {

	@Pure
	public static double division(double a, double b) {
		return a / b;
	}

	@Pure
	public static float division(float a, float b) {
		return a / b;
	}

	@Pure
	public static double multiplication(double a, double b) {
		return a * b;
	}

	@Pure
	public static float multiplication(float a, float b) {
		return a * b;
	}

	@Pure
	public static double circleArea(double r) {
		return 3.14 * r * r;
	}

}
