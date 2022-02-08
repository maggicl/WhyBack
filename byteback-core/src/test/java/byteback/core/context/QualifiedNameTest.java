package byteback.core.context;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class QualifiedNameTest {

    @Test
    public void ToString_OnSimpleQualifiedName_ReturnsCorrectPackageName() {
        final QualifiedName qualifiedName = new QualifiedName("simple", "Name");
        assertEquals(qualifiedName.toString(), "simple.Name");
    }

    @Test
    public void Validate_OnInvalidQualifiedName_ReturnsFalse() {
        QualifiedName qualifiedName;
        qualifiedName = new QualifiedName("Simple", "name");
        assertFalse(qualifiedName.validate());
        qualifiedName = new QualifiedName("", "Name");
        assertFalse(qualifiedName.validate());
        qualifiedName = new QualifiedName("666", "Name");
        assertFalse(qualifiedName.validate());
        qualifiedName = new QualifiedName("42simple", "Name");
        assertFalse(qualifiedName.validate());
        qualifiedName = new QualifiedName("simple*", "Name");
        assertFalse(qualifiedName.validate());
        qualifiedName = new QualifiedName("simple.", "Name");
        assertFalse(qualifiedName.validate());
    }

    @Test
    public void Validate_OnValidQualifiedName_ReturnsTrue() {
        QualifiedName qualifiedName;
        qualifiedName = new QualifiedName("simple", "Name");
        assertTrue(qualifiedName.validate());
        qualifiedName = new QualifiedName("more", "complex", "Name");
        assertTrue(qualifiedName.validate());
        qualifiedName = new QualifiedName("more", "Complex", "Name");
        assertTrue(qualifiedName.validate());
        qualifiedName = new QualifiedName("more", "complex42", "Name");
        assertTrue(qualifiedName.validate());
    }

}
