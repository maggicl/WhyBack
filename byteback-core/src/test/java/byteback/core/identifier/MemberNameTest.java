package byteback.core.identifier;

import org.junit.Test;

public class MemberNameTest {

    @Test(expected = AssertionError.class)
    public void Constructor_OnQualifiedName_ThrowsAssertionError() {
        new MemberName("qualified.name");
    }

    @Test(expected = AssertionError.class)
    public void Constructor_OnSimpleNameStartingWithUpperCaseLetter_ThrowsAssertionError() {
        new MemberName("Test");
    }

    @Test
    public void Constructor_OnSimpleNameStartingWithLowerCaseLetter_DoesNotThrowAssertions() {
        new MemberName("test");
    }

}
