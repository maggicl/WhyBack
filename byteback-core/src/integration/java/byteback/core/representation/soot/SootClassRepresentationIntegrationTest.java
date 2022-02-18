package byteback.core.representation.soot;

import byteback.core.identifier.MethodName;
import byteback.core.identifier.Name;
import byteback.core.type.soot.SootTypeVisitor;
import org.junit.Test;

import soot.RefType;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class SootClassRepresentationIntegrationTest extends SootClassRepresentationFixture {

    @Test
    public void GetName_GivenUnitClass_ReturnsCorrectName() {
        final Name unitName = new Name("byteback.dummy.Unit");
        final SootClassRepresentation classRepresentation = getClass("java8", unitName);
        assertEquals(classRepresentation.getName(), unitName);
    }

    @Test
    public void GetType_GivenUnitClass_ReturnsCorrectSootType() {
        final Name unitName = new Name("byteback.dummy.Unit");
        final SootClassRepresentation classRepresentation = getClass("java8", unitName);
        final SootTypeVisitor visitor = mock(SootTypeVisitor.class);
        classRepresentation.getType().apply(visitor);
        verify(visitor).caseRefType(RefType.v("byteback.dummy.Unit"));
    }

    @Test
    public void GetType_GivenSupportedClass_ReturnsCorrectSootType() {
        final Name supportedName = new Name("byteback.dummy.Supported");
        final SootClassRepresentation classRepresentation = getClass("java8", supportedName);
        final SootTypeVisitor visitor = mock(SootTypeVisitor.class);
        classRepresentation.getType().apply(visitor);
        verify(visitor).caseRefType(RefType.v("byteback.dummy.Supported"));
    }

    @Test
    public void Methods_GivenUnitClass_ReturnsStreamWithConstructor() {
        final Name unitName = new Name("byteback.dummy.Unit");
        final SootClassRepresentation classRepresentation = getClass("java8", unitName);
        assertTrue(classRepresentation.methods().anyMatch((method) -> method.getName().equals(new MethodName("<init>"))));
    }

    @Test
    public void Methods_GivenStaticInitializerClass_ReturnsStreamWithClassInitializer() {
        final Name unitName = new Name("byteback.dummy.StaticInitializer");
        final SootClassRepresentation classRepresentation = getClass("java8", unitName);
        assertTrue(classRepresentation.methods().anyMatch((method) -> method.getName().equals(new MethodName("<clinit>"))));
    }

    @Test
    public void GetName_GivenUnitClass_ReturnsUnitName() {
        final Name unitName = new Name("byteback.dummy.Unit");
        final SootClassRepresentation classRepresentation = getClass("java8", unitName);
        assertEquals(unitName, classRepresentation.getName());
    }

}
