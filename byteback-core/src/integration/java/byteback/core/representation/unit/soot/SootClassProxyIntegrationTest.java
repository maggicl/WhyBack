package byteback.core.representation.unit.soot;

import byteback.core.representation.type.soot.SootTypeVisitor;
import org.junit.Test;

import soot.RefType;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SootClassProxyIntegrationTest extends SootClassProxyFixture {

    @Test
    public void GetName_GivenUnitClass_ReturnsCorrectName() {
        final String unitName = "byteback.dummy.Unit";
        final SootClassProxy classProxy = getClassProxy("java8", unitName);
        assertEquals(classProxy.getName(), unitName);
    }

    @Test
    public void GetType_GivenUnitClass_ReturnsCorrectSootType() {
        final String unitName = "byteback.dummy.Unit";
        final SootClassProxy classProxy = getClassProxy("java8", unitName);
        final SootTypeVisitor visitor = mock(SootTypeVisitor.class);
        classProxy.getType().apply(visitor);
        verify(visitor).caseRefType(RefType.v("byteback.dummy.Unit"));
    }

    @Test
    public void GetType_GivenSupportedClass_ReturnsCorrectSootType() {
        final String supportedName = "byteback.dummy.Supported";
        final SootClassProxy classProxy = getClassProxy("java8", supportedName);
        final SootTypeVisitor visitor = mock(SootTypeVisitor.class);
        classProxy.getType().apply(visitor);
        verify(visitor).caseRefType(RefType.v("byteback.dummy.Supported"));
    }

    @Test
    public void Methods_GivenUnitClass_ReturnsStreamWithConstructor() {
        final String unitName = "byteback.dummy.Unit";
        final SootClassProxy classProxy = getClassProxy("java8", unitName);
        assertTrue(classProxy.methods().anyMatch((method) -> method.getName().equals("<init>")));
    }

    @Test
    public void Methods_GivenStaticInitializerClass_ReturnsStreamWithClassInitializer() {
        final String unitName = "byteback.dummy.StaticInitializer";
        final SootClassProxy classProxy = getClassProxy("java8", unitName);
        assertTrue(classProxy.methods().anyMatch((method) -> method.getName().equals("<clinit>")));
    }

    @Test
    public void GetName_GivenUnitClass_ReturnsUnitName() {
        final String unitName = "byteback.dummy.Unit";
        final SootClassProxy classProxy = getClassProxy("java8", unitName);
        assertEquals(unitName, classProxy.getName());
    }

}
