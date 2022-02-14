package byteback.core.identifier;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class RelativeNameTest {

    @Test(expected=IllegalArgumentException.class)
    public void StringConstructor_OnInvalidName_ThrowsIllegalArgumentException() {
        RelativeName.get("simple.something");
    }

    @Test
    public void ToString_OnSimpleRelativeName_ReturnsCorrectPackageName() {
        final RelativeName relativeName = RelativeName.get("simple");
        assertEquals(relativeName.toString(), "simple");
    }
    
}
