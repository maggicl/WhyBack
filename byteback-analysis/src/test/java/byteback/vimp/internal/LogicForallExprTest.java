package byteback.vimp.internal;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Test;

import byteback.vimp.LogicExprSwitch;
import soot.Local;
import soot.util.Chain;

public class LogicForallExprTest {

	@Test
	public void Apply_DefaultLogicExprSwitch_CallsCaseMethod() {
		final LogicExpr a = mock(LogicExpr.class);
		@SuppressWarnings("unchecked")
		final Chain<Local> b = mock(Chain.class);
		final var v = new LogicForallExpr(b, a);
		final LogicExprSwitch sw = mock(LogicExprSwitch.class);
		v.apply(sw);
		verify(sw).caseLogicForallExpr(v);
	}

}
