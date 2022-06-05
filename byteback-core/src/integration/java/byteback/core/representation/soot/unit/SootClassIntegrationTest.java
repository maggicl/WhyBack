package byteback.core.representation.soot.unit;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import byteback.core.representation.soot.type.SootTypeVisitor;
import org.junit.Test;
import soot.RefType;

public class SootClassIntegrationTest extends SootClassFixture {

	@Test
	public void GetName_GivenUnitClass_ReturnsCorrectName() {
		final String unitName = "byteback.dummy.context.Unit";
		final SootClass clazz = getSootClass("java8", unitName);
		assertEquals(clazz.getName(), unitName);
	}

	@Test
	public void GetType_GivenUnitClass_ReturnsCorrectSootType() {
		final String unitName = "byteback.dummy.context.Unit";
		final SootClass clazz = getSootClass("java8", unitName);
		final SootTypeVisitor<?> visitor = mock(SootTypeVisitor.class);
		clazz.getType().apply(visitor);
		verify(visitor).caseRefType(RefType.v(unitName));
	}

	@Test
	public void GetType_GivenSupportedClass_ReturnsCorrectSootType() {
		final String supportedName = "byteback.dummy.context.Supported";
		final SootClass clazz = getSootClass("java8", supportedName);
		final SootTypeVisitor<?> visitor = mock(SootTypeVisitor.class);
		clazz.getType().apply(visitor);
		verify(visitor).caseRefType(RefType.v("byteback.dummy.context.Supported"));
	}

	@Test
	public void Methods_GivenUnitClass_ReturnsStreamWithConstructor() {
		final String unitName = "byteback.dummy.context.Unit";
		final SootClass clazz = getSootClass("java8", unitName);
		assertTrue(clazz.methods().anyMatch((method) -> method.getName().equals("<init>")));
	}

	@Test
	public void Methods_GivenStaticInitializerClass_ReturnsStreamWithClassInitializer() {
		final String unitName = "byteback.dummy.context.StaticInitializer";
		final SootClass clazz = getSootClass("java8", unitName);
		assertTrue(clazz.methods().anyMatch((method) -> method.getName().equals("<clinit>")));
	}

	@Test
	public void GetName_GivenUnitClass_ReturnsUnitName() {
		final String unitName = "byteback.dummy.context.Unit";
		final SootClass clazz = getSootClass("java8", unitName);
		assertEquals(unitName, clazz.getName());
	}

}
