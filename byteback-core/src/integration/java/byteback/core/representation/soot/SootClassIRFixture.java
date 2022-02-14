package byteback.core.representation.soot;

import byteback.core.ResourcesUtil;
import byteback.core.context.soot.SootContextFixture;
import byteback.core.type.Name;

public class SootClassIRFixture extends SootContextFixture {

    public SootClassIR getClass(final String jarName, final Name className) {
        try {
            getContext().prependClassPath(ResourcesUtil.getJarPath(jarName));

            return getContext().loadClass(className);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

}
