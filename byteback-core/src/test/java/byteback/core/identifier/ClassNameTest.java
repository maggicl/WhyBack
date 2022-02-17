package byteback.core.identifier;

import org.junit.Test;

public class ClassNameTest {

    @Test(expected = AssertionError.class)
    public void Constructor_OnHeadStartingWithLowerCaseLetter_ThrowsAssertionError() {
        new ClassName("test.class");
    }

    @Test
    public void Constructor_OnValidHead_DoesNotThrowExceptions() {
        new ClassName("test.Class");
    }

}
