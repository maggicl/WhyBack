package byteback.core.context;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

import byteback.core.identifier.ClassName;
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
        assertEquals(classPath.toAbsolutePath(), prependedPath);
    }

    @Test
    public void PrependClasspath_WithEmptyPath_ModifiesSootClasspath() {
        final Path classPath = Paths.get("");
        context.prependClassPath(classPath);
        final Path prependedPath = context.getClassPath().get(0);
        assertEquals(classPath.toAbsolutePath(), prependedPath);
    }

    @Test
    public void PrependClasspath_WithRelativePath_ModifiesSootClasspath() {
        final Path classPath = Paths.get(".");
        context.prependClassPath(classPath);
        final Path prependedPath = context.getClassPath().get(0);
        assertEquals(classPath.toAbsolutePath(), prependedPath);
    }

    @Test
    public void Reset_AfterLoadingUnitClass_ResetsClassesCount() throws FileNotFoundException, ClassLoadException {
        final Path classPath = ResourcesUtil.getJarPath("java8");
        final int oldCount = context.getClassesCount();
        context.prependClassPath(classPath);
        final ClassName unitName = new ClassName("byteback", "dummy", "java8", "Unit");
        context.loadClass(unitName);
        context.reset();
        assertEquals(oldCount, context.getClassesCount());
    }

    @Test
    public void LoadClass_OnUnitClass_IncreasesClassesCountBy1() throws FileNotFoundException, ClassLoadException {
        final Path classPath = ResourcesUtil.getJarPath("java8");
        final int oldCount = context.getClassesCount();
        context.prependClassPath(classPath);
        final ClassName unitName = new ClassName("byteback", "dummy", "java8", "Unit");
        context.loadClass(unitName);
        final int newCount = context.getClassesCount();
        assertEquals(oldCount, newCount - 1);
    }

    @Test(expected = ClassLoadException.class)
    public void LoadClass_OnNonExistentClass_ThrowsClassLoadException()
            throws FileNotFoundException, ClassLoadException {
        final Path classPath = ResourcesUtil.getJarPath("java8");
        context.prependClassPath(classPath);
        final ClassName nonExistentName = new ClassName("byteback", "dummy", "java8", "AAAAA");
        context.loadClass(nonExistentName);
    }

    @Test
    public void LoadClassAndSupport_OnSupportedClass_IncreasesClassesCountBy2()
            throws FileNotFoundException, ClassLoadException {
        final Path classPath = ResourcesUtil.getJarPath("java8");
        final int oldCount = context.getClassesCount();
        context.prependClassPath(classPath);
        final ClassName supportedName = new ClassName("byteback", "dummy", "java8", "Supported");
        context.loadClassAndSupport(supportedName);
        final int newCount = context.getClassesCount();
        assertEquals(oldCount, newCount - 2);
    }

    @Test(expected = ClassLoadException.class)
    public void LoadClassAndSupport_OnNonExistentClass_ThrowsClassLoadException()
            throws FileNotFoundException, ClassLoadException {
        final Path classPath = ResourcesUtil.getJarPath("java8");
        context.prependClassPath(classPath);
        final ClassName nonExistentName = new ClassName("byteback", "dummy", "java8", "AAAAA");
        context.loadClassAndSupport(nonExistentName);
    }

}
