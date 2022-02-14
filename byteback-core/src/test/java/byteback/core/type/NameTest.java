package byteback.core.type;

import org.junit.Test;

import static org.junit.Assert.*;

public class NameTest {

    @Test(expected = IllegalArgumentException.class)
    public void Get_OnEmptyName_ThrowsIllegalArgumentException() {
        Name.get("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void Get_OnNameStartingWithNumber_ThrowsIllegalArgumentException() {
        Name.get("666", "test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void Get_OnNameStartingWithMinus_ThrowsIllegalArgumentException() {
        Name.get("-simple", "test");
    }

    @Test(expected = IllegalArgumentException.class)
    public void Get_OnNameEndingWithMinus_ThrowsIllegalArgumentException() {
        Name.get("simple", "test-");
    }

    @Test(expected = IllegalArgumentException.class)
    public void Get_OnNameContainingTrue_ThrowsIllegalArgumentException() {
        Name.get("simple", "true");
    }

    @Test
    public void Get_OnValidQualifiedName_DoesNotThrowException() {
        Name.get("simple", "Name");
    }

    @Test
    public void Get_OnQualifiedNameEndingWithNumber_DoesNotThrowExceptions() {
        Name.get("more", "complex42", "Name");
    }

    @Test
    public void StartsWith_OnNamePrefix_ReturnsTrue() {
        final Name name = Name.get("soot.SootClass");
        final Name prefix = Name.get("soot");

        assertTrue(name.startsWith(prefix));
    }

    @Test
    public void ToString_OnSimpleQualifiedName_ReturnsCorrectPackageName() {
        final Name name = Name.get("simple", "Name");
        assertEquals(name.toString(), "simple.Name");
    }

}
