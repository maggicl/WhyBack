package byteback.core.representation.unit.soot;

import byteback.core.ResourcesUtil;
import byteback.core.context.soot.SootContextFixture;

public class SootClassUnitFixture extends SootContextFixture {

    public SootClassUnit getClassUnit(final String jarName, final String className) {
        try {
            getContext().prependClassPath(ResourcesUtil.getJarPath(jarName));

            return getContext().loadClassAndSupport(className);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

}