package byteback.core.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.nio.file.Path;

import org.junit.After;
import org.junit.Test;

import byteback.core.ResourcesUtil;

public class SootContextIntegrationTest {

    final SootContext context = SootContext.instance();

    @After
    public void resetContext() {
        context.reset();
    }

    @Test
    public void Instance_CalledTwice_ReturnsSameContext() {
        final SootContext a = SootContext.instance();
        final SootContext b = SootContext.instance();
        assertSame(a, b);
    }

    @Test
    public void Reset_AfterLoadClass_ResetsClassesCountTo0() throws FileNotFoundException {
        final Path classPath = ResourcesUtil.getJarPath("java8");
        final int oldCount = context.getClassesCount();
        context.prependClassPath(classPath);
        final QualifiedName unitName = new QualifiedName("byteback", "dummy", "java8", "Unit");
        context.loadClass(unitName);
        context.reset();
        assertEquals(oldCount, context.getClassesCount());
    }

    @Test
    public void LoadClass_OnUnitClass_IncreasesClassesCountBy1() throws FileNotFoundException {
        final Path classPath = ResourcesUtil.getJarPath("java8");
        final int oldCount = context.getClassesCount();
        context.prependClassPath(classPath);
        final QualifiedName unitPath = new QualifiedName("byteback", "dummy", "java8", "Unit");
        context.loadClass(unitPath);
        final int newCount = context.getClassesCount();
        assertTrue(oldCount == newCount - 1);
    }

}
