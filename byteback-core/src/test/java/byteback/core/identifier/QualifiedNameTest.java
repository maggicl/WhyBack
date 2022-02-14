package byteback.core.identifier;

import org.junit.Test;

import static org.junit.Assert.*;

public class QualifiedNameTest {

    @Test
    public void ToString_OnSimpleQualifiedName_ReturnsCorrectPackageName() {
        final QualifiedName qualifiedName = QualifiedName.get("simple", "Name");
        assertEquals(qualifiedName.toString(), "simple.Name");
    }

    @Test
    public void StartsWith_OnNamePrefix_ReturnsTrue() {
        final QualifiedName qualifiedName = QualifiedName.get("soot.SootClass");
        final QualifiedName prefix = QualifiedName.get("soot");

        assertTrue(qualifiedName.startsWith(prefix));
    }

    @Test(expected = IllegalArgumentException.class)
    public void Get_OnEmptyName_ThrowsIllegalArgumentException() {
        QualifiedName.get("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void Get_OnNameStartingWithNumber_ThrowsIllegalArgumentException() {
        QualifiedName.get("666", "test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void Get_OnNameStartingWithMinus_ThrowsIllegalArgumentException() {
        QualifiedName.get("-simple", "test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void Get_OnNameEndingWithMinus_ThrowsIllegalArgumentException() {
        QualifiedName.get("simple", "test-");
    }

    @Test(expected = IllegalArgumentException.class)
    public void Get_OnNameContainingTrue_ThrowsIllegalArgumentException() {
        QualifiedName.get("simple", "true");
    }

    @Test
    public void Get_OnValidQualifiedName_DoesNotThrowException() {
        QualifiedName.get("simple", "Name");
    }

    @Test
    public void Get_OnQualifiedNameEndingWithNumber_DoesNotThrowExceptions() {
        QualifiedName.get("more", "complex42", "Name");
    }

    @Test
    public void IsPrefixedBy_OnPrefixPattern_ReturnsTrue() {
        QualifiedName qualifiedName = QualifiedName.get("simple", "prefix");
        assertTrue(qualifiedName.startsWith("simple"));
        assertTrue(qualifiedName.startsWith(QualifiedName.get("simple")));
    }

}
