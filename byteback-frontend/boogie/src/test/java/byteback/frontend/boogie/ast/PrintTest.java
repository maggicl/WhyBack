package byteback.frontend.boogie.ast;

import org.junit.Test;

public class PrintTest extends ASTTestFixture {

    @Test
    public void Print_GivenUnitProgram_DoesNotThrowException() {
        final Program program = getProgram("Unit");
        final StringBuilder builder = new StringBuilder();
        program.print(builder);
    }

}
