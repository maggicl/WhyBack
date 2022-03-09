package byteback.core.representation.soot;

import byteback.core.ResourcesUtil;
import byteback.core.context.soot.SootContextFixture;
import byteback.core.identifier.Name;

public class SootClassRepresentationFixture extends SootContextFixture {

    public SootClassRepresentation getClass(final String jarName, final Name className) {
        try {
            getContext().prependClassPath(ResourcesUtil.getJarPath(jarName));

            return getContext().loadClassAndSupport(className);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }

}
