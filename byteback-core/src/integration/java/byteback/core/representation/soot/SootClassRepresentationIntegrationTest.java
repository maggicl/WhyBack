package byteback.core.representation.soot;

import byteback.core.type.Name;
import byteback.core.type.soot.SootTypeVisitor;
import org.junit.Test;

import soot.RefType;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class SootClassRepresentationIntegrationTest extends SootClassRepresentationFixture {

    @Test
    public void GetName_GivenUnitClass_ReturnsCorrectName() {
        final Name unitName = Name.get("byteback", "dummy", "java8", "Unit");
        final SootClassRepresentation classRepresentation = getClass("java8", unitName);
        assertEquals(classRepresentation.getName(), unitName);
    }

    @Test
    public void GetType_GivenUnitClass_ReturnsCorrectSootType() {
        final Name unitName = Name.get("byteback", "dummy", "java8", "Unit");
        final SootClassRepresentation classRepresentation = getClass("java8", unitName);
        final SootTypeVisitor visitor = mock(SootTypeVisitor.class);
        classRepresentation.getType().apply(visitor);
        verify(visitor).caseRefType(RefType.v("byteback.dummy.java8.Unit"));
    }

}
