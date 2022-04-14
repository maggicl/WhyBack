package byteback.core.context.soot;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import byteback.core.ResourcesUtil;
import byteback.core.representation.soot.unit.SootClass;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.After;
import org.junit.Test;

public class SootContextIntegrationTest extends SootContextFixture {

	@After
	public void after() {
		resetContext();
	}

	@Test
	public void PrependClasspath_WithValidClasspath_ModifiesSootClasspath() {
		final Path classPath = Paths.get("test", "class", "path");
		getContext().prependClassPath(classPath);
		final Path prependedPath = getContext().getSootClassPath().get(0);
		assertEquals(classPath.toAbsolutePath(), prependedPath);
	}

	@Test
	public void PrependClasspath_WithCurrentDirectoryPath_AppendsCurrentDirectoryToSootClasspath() {
		final Path classPath = Paths.get(".");
		getContext().prependClassPath(classPath);
		final Path prependedPath = getContext().getSootClassPath().get(0);
		assertEquals(classPath.toAbsolutePath(), prependedPath);
	}

	@Test
	public void PrependClasspath_WithEmptyPath_AppendsCurrentDirectoryToSootClasspath() {
		final Path classPath = Paths.get("");
		getContext().prependClassPath(classPath);
		final Path prependedPath = getContext().getSootClassPath().get(0);
		assertEquals(classPath.toAbsolutePath(), prependedPath);
	}

	@Test
	public void Reset_AfterLoadingUnitClass_ResetsClassesCount() throws FileNotFoundException, ClassLoadException {
		final Path classPath = ResourcesUtil.getJarPath("java8");
		final int oldCount = getContext().getClassesCount();
		getContext().prependClassPath(classPath);
		getContext().loadClass("byteback.dummy.context.Unit");
		getContext().reset();
		assertEquals(oldCount, getContext().getClassesCount());
	}

	@Test
	public void LoadClass_OnUnitClass_IncreasesClassesCountBy1() throws FileNotFoundException, ClassLoadException {
		final Path classPath = ResourcesUtil.getJarPath("java8");
		final int oldCount = getContext().getClassesCount();
		getContext().prependClassPath(classPath);
		getContext().loadClass("byteback.dummy.context.Unit");
		final int newCount = getContext().getClassesCount();
		assertEquals(oldCount, newCount - 1);
	}

	@Test
	public void LoadClass_OnUnitClass_ReturnsUnitClass() throws FileNotFoundException, ClassLoadException {
		final Path classPath = ResourcesUtil.getJarPath("java8");
		final String name = "byteback.dummy.context.Unit";
		getContext().prependClassPath(classPath);
		final SootClass clazz = getContext().loadClass(name);
		assertEquals(clazz.getName(), name);
	}

	@Test
	public void LoadClassAndSupport_OnUnitClass_ReturnsUnitClass() throws FileNotFoundException, ClassLoadException {
		final Path classPath = ResourcesUtil.getJarPath("java8");
		final String name = "byteback.dummy.context.Unit";
		getContext().prependClassPath(classPath);
		final SootClass clazz = getContext().loadClassAndSupport("byteback.dummy.context.Unit");
		assertEquals(clazz.getName(), name);
	}

	@Test(expected = ClassLoadException.class)
	public void LoadClass_OnNonExistentClass_ThrowsClassLoadException()
			throws FileNotFoundException, ClassLoadException {
		final Path classPath = ResourcesUtil.getJarPath("java8");
		final String nonExistentName = "byteback.dummy.context.AAAAA";
		getContext().prependClassPath(classPath);
		getContext().loadClass(nonExistentName);
	}

	@Test
	public void LoadClassAndSupport_OnSupportedClass_IncreasesClassesCountBy2()
			throws FileNotFoundException, ClassLoadException {
		final Path classPath = ResourcesUtil.getJarPath("java8");
		final int oldCount = getContext().getClassesCount();
		getContext().prependClassPath(classPath);
		getContext().loadClassAndSupport("byteback.dummy.context.Supported");
		final int newCount = getContext().getClassesCount();
		assertEquals(oldCount, newCount - 2);
	}

	@Test(expected = ClassLoadException.class)
	public void LoadClassAndSupport_OnNonExistentClass_ThrowsClassLoadException()
			throws FileNotFoundException, ClassLoadException {
		final Path classPath = ResourcesUtil.getJarPath("java8");
		final String nonExistentName = "byteback.dummy.context.AAAAA";
		getContext().prependClassPath(classPath);
		getContext().loadClassAndSupport(nonExistentName);
	}

	@Test
	public void Classes_GivenUnloadedScene_ReturnsNonEmptyStream() {
		assertTrue(getContext().classes().findAny().isPresent());
	}

	@Test
	public void Classes_GivenUnloadedScene_ReturnsBasicClassesStream() {
		assertTrue(getContext().classes().allMatch(SootClass::isBasicClass));
	}

	@Test
	public void Classes_GivenUnloadedScene_ReturnsConcreteClassesStream() {
		assertTrue(getContext().classes().noneMatch(SootClass::isPhantomClass));
	}

	@Test
	public void Classes_AfterLoadingUnitClass_ReturnsStreamContainingUnitClass()
			throws FileNotFoundException, ClassLoadException {
		final Path classPath = ResourcesUtil.getJarPath("java8");
		final String name = "byteback.dummy.context.StaticInitializer";
		getContext().prependClassPath(classPath);
		getContext().loadClass(name);
		assertTrue(getContext().classes().anyMatch((clazz) -> clazz.getName().equals(name)));
	}

}
