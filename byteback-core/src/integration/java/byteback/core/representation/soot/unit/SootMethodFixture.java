package byteback.core.representation.soot.unit;

public class SootMethodFixture extends SootClassFixture {

	public static SootMethod getMethodUnit(final String jarName, final String className,
                                           final String methodSignature) {

		return getClassUnit(jarName, className).methods().filter((unit) -> unit.getIdentifier().equals(methodSignature))
				.findFirst().orElseThrow(() -> {
					throw new IllegalArgumentException("Cannot find method with signature " + methodSignature);
				});
	}

}
