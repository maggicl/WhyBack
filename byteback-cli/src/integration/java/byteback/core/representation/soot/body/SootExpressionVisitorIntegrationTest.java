package byteback.core.representation.soot.body;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Test;

public class SootExpressionVisitorIntegrationTest {

	@Test
	public void DefaultCase_CallsCaseDefault() {
		final SootExpressionVisitor<?> visitor = spy(SootExpressionVisitor.class);
		visitor.defaultCase(null);
		verify(visitor).caseDefault(null);
	}

}
