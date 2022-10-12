package byteback.core.representation.soot.body;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Test;

public class SootStatementVisitorIntegrationTest {

	@Test
	public void DefaultCase_CallsCaseDefault() {
		final SootStatementVisitor<?> visitor = spy(SootStatementVisitor.class);
		visitor.defaultCase(null);
		verify(visitor).caseDefault(null);
	}

}
