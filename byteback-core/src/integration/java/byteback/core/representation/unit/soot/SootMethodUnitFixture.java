package byteback.core.representation.unit.soot;

public class SootMethodUnitFixture extends SootClassUnitFixture {

    public static SootMethodUnit getMethodUnit(final String jarName, final String className, final String methodSignature) {
        return getClassUnit(jarName, className).methods().filter((unit) -> unit.getIdentifier().equals(methodSignature))
                .findFirst().orElseThrow(() -> {
                    throw new IllegalArgumentException("Cannot find method with signature " + methodSignature);
                });
    }

}
