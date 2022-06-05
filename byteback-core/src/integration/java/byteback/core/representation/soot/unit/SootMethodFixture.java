package byteback.core.representation.soot.unit;

public class SootMethodFixture extends SootClassFixture {

	public static SootMethod getSootMethod(final String jarName, final String className, final String methodSignature) {

		return getSootClass(jarName, className).methods().filter((unit) -> unit.getIdentifier().equals(methodSignature))
				.findFirst().orElseThrow(
						() -> new IllegalArgumentException("Cannot find method with signature " + methodSignature));
	}

}
