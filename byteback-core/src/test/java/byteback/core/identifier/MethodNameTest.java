package byteback.core.identifier;

import org.junit.Test;

public class MethodNameTest {

    @Test
    public void Constructor_OnInitName_DoesNotThrowExceptions() {
        new MethodName("<init>");
    }

    @Test
    public void Constructor_OnClassInitName_DoesNotThrowExceptions() {
        new MethodName("<clinit>");
    }

}
