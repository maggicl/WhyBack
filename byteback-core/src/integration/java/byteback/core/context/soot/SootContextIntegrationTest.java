package byteback.core.context.soot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

import byteback.core.context.ClassLoadException;
import byteback.core.representation.unit.soot.SootClassUnit;
import org.junit.Test;

import byteback.core.ResourcesUtil;

public class SootContextIntegrationTest extends SootContextFixture {

    @Test
    public void PrependClasspath_WithValidClasspath_ModifiesSootClasspath() {
        final Path classPath = Paths.get("test", "class", "path");
        getContext().prependClassPath(classPath);
        final Path prependedPath = getContext().getClassPath().get(0);
        assertEquals(classPath.toAbsolutePath(), prependedPath);
    }

    @Test
    public void PrependClasspath_WithCurrentDirectoryPath_AppendsCurrentDirectoryToSootClasspath() {
        final Path classPath = Paths.get(".");
        getContext().prependClassPath(classPath);
        final Path prependedPath = getContext().getClassPath().get(0);
        assertEquals(classPath.toAbsolutePath(), prependedPath);
    }

    @Test
    public void PrependClasspath_WithEmptyPath_AppendsCurrentDirectoryToSootClasspath() {
        final Path classPath = Paths.get("");
        getContext().prependClassPath(classPath);
        final Path prependedPath = getContext().getClassPath().get(0);
        assertEquals(classPath.toAbsolutePath(), prependedPath);
    }

    @Test
    public void Reset_AfterLoadingUnitClass_ResetsClassesCount() throws FileNotFoundException, ClassLoadException {
        final Path classPath = ResourcesUtil.getJarPath("java8");
        final int oldCount = getContext().getClassesCount();
        getContext().prependClassPath(classPath);
        getContext().loadClass("byteback.dummy.Unit");
        getContext().reset();
        assertEquals(oldCount, getContext().getClassesCount());
    }

    @Test
    public void LoadClass_OnUnitClass_IncreasesClassesCountBy1() throws FileNotFoundException, ClassLoadException {
        final Path classPath = ResourcesUtil.getJarPath("java8");
        final int oldCount = getContext().getClassesCount();
        getContext().prependClassPath(classPath);
        getContext().loadClass("byteback.dummy.Unit");
        final int newCount = getContext().getClassesCount();
        assertEquals(oldCount, newCount - 1);
    }

    @Test
    public void LoadClass_OnUnitClass_ReturnUnitClassRepresentation() throws FileNotFoundException, ClassLoadException {
        final Path classPath = ResourcesUtil.getJarPath("java8");
        final String unitName = "byteback.dummy.Unit";
        getContext().prependClassPath(classPath);
        final SootClassUnit classUnit = getContext().loadClass(unitName);
        assertEquals(classUnit.getName(), unitName);
    }

    @Test
    public void LoadClassAndSupport_OnUnitClass_ReturnUnitClassRepresentation()
            throws FileNotFoundException, ClassLoadException {
        final Path classPath = ResourcesUtil.getJarPath("java8");
        final String unitName = "byteback.dummy.Unit";
        getContext().prependClassPath(classPath);
        final SootClassUnit classUnit = getContext().loadClassAndSupport("byteback.dummy.Unit");
        assertEquals(classUnit.getName(), unitName);
    }

    @Test(expected = ClassLoadException.class)
    public void LoadClass_OnNonExistentClass_ThrowsClassLoadException()
            throws FileNotFoundException, ClassLoadException {
        final Path classPath = ResourcesUtil.getJarPath("java8");
        final String nonExistentName = "byteback.dummy.AAAAA";
        getContext().prependClassPath(classPath);
        getContext().loadClass(nonExistentName);
    }

    @Test
    public void LoadClassAndSupport_OnSupportedClass_IncreasesClassesCountBy2()
            throws FileNotFoundException, ClassLoadException {
        final Path classPath = ResourcesUtil.getJarPath("java8");
        final int oldCount = getContext().getClassesCount();
        getContext().prependClassPath(classPath);
        getContext().loadClassAndSupport("byteback.dummy.Supported");
        final int newCount = getContext().getClassesCount();
        assertEquals(oldCount, newCount - 2);
    }

    @Test(expected = ClassLoadException.class)
    public void LoadClassAndSupport_OnNonExistentClass_ThrowsClassLoadException()
            throws FileNotFoundException, ClassLoadException {
        final Path classPath = ResourcesUtil.getJarPath("java8");
        final String nonExistentName = "byteback.dummy.AAAAA";
        getContext().prependClassPath(classPath);
        getContext().loadClassAndSupport(nonExistentName);
    }

    @Test
    public void Classes_GivenUnloadedScene_ReturnsNonEmptyStream() {
        assertTrue(getContext().classes().findAny().isPresent());
    }

    @Test
    public void Classes_GivenUnloadedScene_ReturnsBasicClassesStream() {
        assertTrue(getContext().classes().allMatch(SootClassUnit::isBasicClass));
    }

    @Test
    public void Classes_GivenUnloadedScene_ReturnsConcreteClassesStream() {
        assertTrue(getContext().classes().noneMatch(SootClassUnit::isPhantomClass));
    }

    @Test
    public void Classes_AfterLoadingUnitClass_ReturnsStreamContainingUnitClass()
            throws FileNotFoundException, ClassLoadException {
        final Path classPath = ResourcesUtil.getJarPath("java8");
        final String unitName = "byteback.dummy.StaticInitializer";
        getContext().prependClassPath(classPath);
        getContext().loadClass(unitName);
        assertTrue(getContext().classes().anyMatch((clazz) -> clazz.getName().equals(unitName)));
    }

}
