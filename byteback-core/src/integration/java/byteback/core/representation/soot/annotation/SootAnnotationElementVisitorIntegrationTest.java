package byteback.core.representation.soot.annotation;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Test;

public class SootAnnotationElementVisitorIntegrationTest {

	@Test
	public void DefaultCase_CallsCaseDefault() {
		final SootAnnotationElemVisitor<?> visitor = spy(SootAnnotationElemVisitor.class);
		visitor.defaultCase(null);
		verify(visitor).caseDefault(null);
	}

}
