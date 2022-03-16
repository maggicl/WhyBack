package byteback.core.converter.soot.boogie;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import byteback.frontend.boogie.ast.PrintUtil;

public class BoogieFunctionExtractorIntegrationTest extends BoogieFunctionExtractorFixture {

    private final Logger log = LoggerFactory.getLogger(BoogieFunctionExtractorFixture.class);

    @Test
    public void Convert_GivenRegressedFunction_ReturnsExpectedResult() {
        getFunctionEntries().forEach((entry) -> {
            assertEquals(PrintUtil.toString(entry.expected), PrintUtil.toString(entry.actual));
            log.info("Comparing the generated function declaration {}", entry.expected.getDeclarator().getName());
        });
    }

}
