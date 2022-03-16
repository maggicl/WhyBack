package byteback.core.converter.soot.boogie;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import byteback.frontend.boogie.ast.PrintUtil;

public class BoogieFunctionExtractorIntegrationTest extends BoogieFunctionExtractorFixture {

    @Test
    public void Convert_GivenRegressedFunction_ReturnsExpectedResult() {
        getFunctionEntries().forEach((entry) -> {
            assertEquals(PrintUtil.toString(entry.expected), PrintUtil.toString(entry.actual));
        });
    }

}
