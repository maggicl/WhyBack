package byteback.core.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.After;
import org.junit.Test;

import byteback.core.identifier.QualifiedName;
import byteback.core.representation.SootClassRepresentation;
import byteback.core.ResourcesUtil;

public class SootContextIntegrationTest {

    private final SootContext context = SootContext.instance();

    @After
    public void resetContext() {
        context.reset();
    }

    @Test
    public void PrependClasspath_WithValidClasspath_ModifiesSootClasspath() {
        final Path classPath = Paths.get("test", "class", "path");
        context.prependClassPath(classPath);
        final Path prependedPath = context.getClassPath().get(0);
        assertEquals(classPath.toAbsolutePath(), prependedPath);
    }

    @Test
    public void PrependClasspath_WithCurrentDirectoryPath_AppendsCurrentDirectoryToSootClasspath() {
        final Path classPath = Paths.get(".");
        context.prependClassPath(classPath);
        final Path prependedPath = context.getClassPath().get(0);
        assertEquals(classPath.toAbsolutePath(), prependedPath);
    }

    @Test
    public void PrependClasspath_WithEmptyPath_AppendsCurrentDirectoryToSootClasspath() {
        final Path classPath = Paths.get("");
        context.prependClassPath(classPath);
        final Path prependedPath = context.getClassPath().get(0);
        assertEquals(classPath.toAbsolutePath(), prependedPath);
    }

    @Test
    public void Reset_AfterLoadingUnitClass_ResetsClassesCount() throws FileNotFoundException, ClassLoadException {
        final Path classPath = ResourcesUtil.getJarPath("java8");
        final int oldCount = context.getClassesCount();
        context.prependClassPath(classPath);
        final QualifiedName unitName = QualifiedName.get("byteback", "dummy", "java8", "Unit");
        context.loadClass(unitName);
        context.reset();
        assertEquals(oldCount, context.getClassesCount());
    }

    @Test
    public void LoadClass_OnUnitClass_IncreasesClassesCountBy1() throws FileNotFoundException, ClassLoadException {
        final Path classPath = ResourcesUtil.getJarPath("java8");
        final int oldCount = context.getClassesCount();
        context.prependClassPath(classPath);
        final QualifiedName unitName = QualifiedName.get("byteback", "dummy", "java8", "Unit");
        context.loadClass(unitName);
        final int newCount = context.getClassesCount();
        assertEquals(oldCount, newCount - 1);
    }

    @Test(expected = ClassLoadException.class)
    public void LoadClass_OnNonExistentClass_ThrowsClassLoadException()
            throws FileNotFoundException, ClassLoadException {
        final Path classPath = ResourcesUtil.getJarPath("java8");
        context.prependClassPath(classPath);
        final QualifiedName nonExistentName = QualifiedName.get("byteback", "dummy", "java8", "AAAAA");
        context.loadClass(nonExistentName);
    }

    @Test
    public void LoadClassAndSupport_OnSupportedClass_IncreasesClassesCountBy2()
            throws FileNotFoundException, ClassLoadException {
        final Path classPath = ResourcesUtil.getJarPath("java8");
        final int oldCount = context.getClassesCount();
        context.prependClassPath(classPath);
        final QualifiedName supportedName = QualifiedName.get("byteback", "dummy", "java8", "Supported");
        context.loadClassAndSupport(supportedName);
        final int newCount = context.getClassesCount();
        assertEquals(oldCount, newCount - 2);
    }

    @Test(expected = ClassLoadException.class)
    public void LoadClassAndSupport_OnNonExistentClass_ThrowsClassLoadException()
            throws FileNotFoundException, ClassLoadException {
        final Path classPath = ResourcesUtil.getJarPath("java8");
        context.prependClassPath(classPath);
        final QualifiedName nonExistentName = QualifiedName.get("byteback", "dummy", "java8", "AAAAA");
        context.loadClassAndSupport(nonExistentName);
    }

    @Test
    public void Classes_GivenUnloadedScene_ReturnsNonEmptyStream() {
        assertTrue(context.classes().findAny().isPresent());
    }

    @Test
    public void Classes_GivenUnloadedScene_ReturnsBasicClassesStream() {
        assertTrue(context.classes().allMatch(SootClassRepresentation::isBasicClass));
    }

    @Test
    public void Classes_GivenUnloadedScene_ReturnsConcreteClassesStream() {
        assertTrue(context.classes().allMatch((clazz) -> !clazz.isPhantomClass()));
    }

    @Test
    public void Classes_AfterLoadingUnitClass_ReturnsStreamContainingUnitClass()
            throws FileNotFoundException, ClassLoadException {
        final Path classPath = ResourcesUtil.getJarPath("java8");
        context.prependClassPath(classPath);
        final QualifiedName unitName = QualifiedName.get("byteback", "dummy", "java8", "Unit");
        context.loadClass(unitName);
        assertTrue(context.classes().anyMatch((clazz) -> {
            return clazz.getQualifiedName().toString().equals(unitName.toString());
        }));
    }

}
