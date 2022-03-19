package byteback.core.representation.soot.unit;

import byteback.core.ResourcesUtil;
import byteback.core.context.soot.SootContextFixture;

public class SootClassUnitFixture extends SootContextFixture {

    public static SootClassUnit getClassUnit(final String jarName, final String className) {
        try {
            getContext().prependClassPath(ResourcesUtil.getJarPath(jarName));

            return getContext().loadClassAndSupport(className);
        } catch (final Exception exception) {
            throw new RuntimeException(exception);
        }
    }

}
