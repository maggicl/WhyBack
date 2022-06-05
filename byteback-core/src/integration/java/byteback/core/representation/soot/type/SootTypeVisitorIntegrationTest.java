package byteback.core.representation.soot.type;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Test;

public class SootTypeVisitorIntegrationTest {

	@Test
	public void DefaultCase_CallsCaseDefault() {
		final SootTypeVisitor<?> visitor = spy(SootTypeVisitor.class);
		visitor.defaultCase(null);
		verify(visitor).caseDefault(null);
	}

}
