package byteback.core.representation.soot.unit;

import soot.SootMethod;

public class SootMethodFixture extends SootClassFixture {

	public static SootMethod getSootMethod(final String jarName, final String className, final String methodSignature) {

		return getSootClass(jarName, className).getMethods().stream()
				.filter((unit) -> unit.getSignature().equals(methodSignature)).findFirst().orElseThrow(
						() -> new IllegalArgumentException("Cannot find method with signature " + methodSignature));
	}

}
