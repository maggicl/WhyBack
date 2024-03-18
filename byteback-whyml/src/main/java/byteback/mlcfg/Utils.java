package byteback.mlcfg;

import java.util.Collections;
import java.util.stream.Stream;

public final class Utils {
	private Utils() {
	}

	public static Stream<String> repeat(int times, String s) {
		return Collections.nCopies(times, s).stream();
	}

	public static String trimToNull(String e) {
		return e.isEmpty() ? null : e;
	}

	public static String doubleToBinary(double d) {

	}

	public static String floatToBinary(float f) {

	}

	public static void main(String[] args) {
		System.out.println(doubleToBinary(5.0));
		System.out.println(doubleToBinary(13.0));
		System.out.println(doubleToBinary(5.0 / 13.0));
		System.out.println(doubleToBinary(10.1));
		System.out.println(doubleToBinary(2.5));
		System.out.println(doubleToBinary(10.1 % 2.5));
		System.out.println(doubleToBinary(10.1 - ((10.1 / 2.5) * 2.5)));

		System.out.println(doubleToBinary(10.1 / 2.5));

		System.out.println(doubleToBinary(1.0 / 0.0));
		System.out.println(doubleToBinary(1.5));
		System.out.println(doubleToBinary(4.0));
		System.out.println(doubleToBinary(Double.MIN_VALUE));
	}
}
