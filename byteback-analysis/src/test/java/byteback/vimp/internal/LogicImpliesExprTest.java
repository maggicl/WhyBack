package byteback.vimp.internal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import byteback.vimp.LogicExprSwitch;

public class LogicImpliesExprTest {

	@Test
	public void Apply_DefaultLogicExprSwitch_CallsCaseMethod() {
		final LogicExpr a = mock(LogicExpr.class);
		final LogicExpr b = mock(LogicExpr.class);
		final var v = new LogicImpliesExpr(a, b);
		final LogicExprSwitch sw = mock(LogicExprSwitch.class);
		v.apply(sw);
		verify(sw).caseLogicImpliesExpr(v);
	}

}
