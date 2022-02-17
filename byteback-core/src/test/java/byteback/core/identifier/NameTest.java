package byteback.core.identifier;

import org.junit.Test;

import static org.junit.Assert.*;

public class NameTest {

    @Test(expected = AssertionError.class)
    public void Constructor_OnEmptyName_ThrowsAssertionError() {
        new Name("");
    }

    @Test(expected = AssertionError.class)
    public void Get_OnNameStartingWithNumber_ThrowsAssertionError() {
        new Name("666.test");
    }

    @Test(expected = AssertionError.class)
    public void Get_OnNameStartingWithMinus_ThrowsAssertionError() {
        new Name("-simple.test");
    }

    @Test(expected = AssertionError.class)
    public void Get_OnNameEndingWithMinus_ThrowsAssertionError() {
        new Name("simple.test-");
    }

    @Test(expected = AssertionError.class)
    public void Get_OnNameContainingTrue_ThrowsAssertionError() {
        new Name("simple.true");
    }

    @Test
    public void Get_OnValidQualifiedName_DoesNotThrowException() {
        new Name("simple.Name");
    }

    @Test
    public void Get_OnQualifiedNameEndingWithNumber_DoesNotThrowExceptions() {
        new Name("more.complex42.Name");
    }

    @Test
    public void StartsWith_OnNamePrefix_ReturnsTrue() {
        final Name name = new Name("soot.SootClass");
        final Name prefix = new Name("soot");

        assertTrue(name.startsWith(prefix));
    }

    @Test
    public void ToString_OnSimpleQualifiedName_ReturnsCorrectQualifiedString() {
        final Name name = new Name("simple.Name");
        assertEquals(name.toString(), "simple.Name");
    }

}
