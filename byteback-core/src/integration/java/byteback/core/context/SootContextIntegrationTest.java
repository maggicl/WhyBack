package byteback.core.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

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
    public void PrependClasspath_ModifiesSootClasspath() {
        final Path classPath = Paths.get("test", "class", "path");
        context.prependClassPath(classPath);
        final Path prependedPath = context.getClassPath().get(0);
        assertEquals(classPath, prependedPath);
    }

    @Test
    public void PrependClasspath_WithEmptyPath_ModifiesSootClasspath() {
        final Path classPath = Paths.get("");
        context.prependClassPath(classPath);
        final Path prependedPath = context.getClassPath().get(0);
        assertEquals(classPath, prependedPath);
    }

    @Test
    public void PrependClasspath_WithRelativePath_ModifiesSootClasspath() {
        final Path classPath = Paths.get(".");
        context.prependClassPath(classPath);
        final Path prependedPath = context.getClassPath().get(0);
        assertEquals(classPath, prependedPath);
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
        final QualifiedName unitName = new QualifiedName("byteback", "dummy", "java8", "Unit");
        context.loadClass(unitName);
        final int newCount = context.getClassesCount();
        assertTrue(oldCount == newCount - 1);
    }

}
