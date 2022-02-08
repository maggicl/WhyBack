package byteback.core.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.nio.file.Path;

import org.junit.Test;

import byteback.core.ResourcesUtil;

public class SootContextIntegrationTest {

    @Test
    public void Instance_CalledTwice_ReturnsSameContext() {
        final SootContext a = SootContext.instance();
        final SootContext b = SootContext.instance();
        assertEquals(a, b);
    }

    @Test
    public void LoadClass_CalledOnce_IncreasesClassesCountBy1() throws FileNotFoundException {
        final Path classPath = ResourcesUtil.getJarPath("java8");
        final SootContext context = SootContext.instance();
        final int oldCount = context.getClassesCount();
        context.prependClassPath(classPath);
        context.loadClass("byteback.dummy.java8.Unit");
        final int newCount = context.getClassesCount();
        assertTrue(oldCount == newCount - 1);
    }

}
