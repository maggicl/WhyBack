package byteback.core.representation.unit.soot;

import byteback.core.ResourcesUtil;
import byteback.core.context.soot.SootContextFixture;

public class SootClassProxyFixture extends SootContextFixture {

    public SootClassProxy getClassProxy(final String jarName, final String className) {
        try {
            getContext().prependClassPath(ResourcesUtil.getJarPath(jarName));

            return getContext().loadClassAndSupport(className);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

}
