package byteback.core.context.soot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;

import byteback.core.context.ClassLoadException;
import org.junit.Test;

import byteback.core.type.Name;
import byteback.core.representation.soot.SootClassIR;
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
        final Name unitName = Name.get("byteback", "dummy", "java8", "Unit");
        getContext().loadClass(unitName);
        getContext().reset();
        assertEquals(oldCount, getContext().getClassesCount());
    }

    @Test
    public void LoadClass_OnUnitClass_IncreasesClassesCountBy1() throws FileNotFoundException, ClassLoadException {
        final Path classPath = ResourcesUtil.getJarPath("java8");
        final int oldCount = getContext().getClassesCount();
        getContext().prependClassPath(classPath);
        final Name unitName = Name.get("byteback", "dummy", "java8", "Unit");
        getContext().loadClass(unitName);
        final int newCount = getContext().getClassesCount();
        assertEquals(oldCount, newCount - 1);
    }

    @Test
    public void LoadClass_OnUnitClass_ReturnUnitClassRepresentation() throws FileNotFoundException, ClassLoadException {
        final Path classPath = ResourcesUtil.getJarPath("java8");
        getContext().prependClassPath(classPath);
        final Name unitName = Name.get("byteback", "dummy", "java8", "Unit");
        final SootClassIR sootClass = getContext().loadClass(unitName);
        assertEquals(sootClass.getName().toString(), unitName.toString());
    }

    @Test
    public void LoadClassAndSupport_OnUnitClass_ReturnUnitClassRepresentation()
            throws FileNotFoundException, ClassLoadException {
        final Path classPath = ResourcesUtil.getJarPath("java8");
        getContext().prependClassPath(classPath);
        final Name unitName = Name.get("byteback", "dummy", "java8", "Unit");
        final SootClassIR sootClass = getContext().loadClassAndSupport(unitName);
        assertEquals(sootClass.getName().toString(), unitName.toString());
    }

    @Test(expected = ClassLoadException.class)
    public void LoadClass_OnNonExistentClass_ThrowsClassLoadException()
            throws FileNotFoundException, ClassLoadException {
        final Path classPath = ResourcesUtil.getJarPath("java8");
        getContext().prependClassPath(classPath);
        final Name nonExistentName = Name.get("byteback", "dummy", "java8", "AAAAA");
        getContext().loadClass(nonExistentName);
    }

    @Test
    public void LoadClassAndSupport_OnSupportedClass_IncreasesClassesCountBy2()
            throws FileNotFoundException, ClassLoadException {
        final Path classPath = ResourcesUtil.getJarPath("java8");
        final int oldCount = getContext().getClassesCount();
        getContext().prependClassPath(classPath);
        final Name supportedName = Name.get("byteback", "dummy", "java8", "Supported");
        getContext().loadClassAndSupport(supportedName);
        final int newCount = getContext().getClassesCount();
        assertEquals(oldCount, newCount - 2);
    }

    @Test(expected = ClassLoadException.class)
    public void LoadClassAndSupport_OnNonExistentClass_ThrowsClassLoadException()
            throws FileNotFoundException, ClassLoadException {
        final Path classPath = ResourcesUtil.getJarPath("java8");
        getContext().prependClassPath(classPath);
        final Name nonExistentName = Name.get("byteback", "dummy", "java8", "AAAAA");
        getContext().loadClassAndSupport(nonExistentName);
    }

    @Test
    public void Classes_GivenUnloadedScene_ReturnsNonEmptyStream() {
        assertTrue(getContext().classes().findAny().isPresent());
    }

    @Test
    public void Classes_GivenUnloadedScene_ReturnsBasicClassesStream() {
        assertTrue(getContext().classes().allMatch(SootClassIR::isBasicClass));
    }

    @Test
    public void Classes_GivenUnloadedScene_ReturnsConcreteClassesStream() {
        assertTrue(getContext().classes().allMatch((clazz) -> !clazz.isPhantomClass()));
    }

    @Test
    public void Classes_AfterLoadingUnitClass_ReturnsStreamContainingUnitClass()
            throws FileNotFoundException, ClassLoadException {
        final Path classPath = ResourcesUtil.getJarPath("java8");
        getContext().prependClassPath(classPath);
        final Name unitName = Name.get("byteback", "dummy", "java8", "Unit");
        getContext().loadClass(unitName);
        assertTrue(getContext().classes().anyMatch((clazz) -> {
            return clazz.getName().toString().equals(unitName.toString());
        }));
    }

}
