package byteback.core.representation.unit.soot;

public class SootMethodUnitFixture extends SootClassUnitFixture {

    public SootMethodUnit getMethodUnit(final String jarName, final String className, final String methodSignature) {
        return getClassUnit(jarName, className).methods().filter((Unit) -> Unit.getIdentifier().equals(methodSignature)).findFirst().get();
    }


}
