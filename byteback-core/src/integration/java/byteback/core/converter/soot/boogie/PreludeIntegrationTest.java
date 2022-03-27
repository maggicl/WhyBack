package byteback.core.converter.soot.boogie;

import static org.junit.Assert.assertEquals;

import byteback.frontend.boogie.ast.*;
import org.junit.Test;

public class PreludeIntegrationTest {

	@Test
	public void InitializeProgram_GivenPrelude_DoesNotThrowExceptions() {
		Prelude.initializeProgram();
	}

	@Test
	public void LoadProgram_CalledTwice_ReturnsTheSameInstance() {
		final Program a = Prelude.loadProgram();
		final Program b = Prelude.loadProgram();
		assertEquals(b, a);
	}

	@Test
	public void GetReferenceType_GivenPrelude_DoesNotThrowException() {
		Prelude.getReferenceType();
	}

	@Test
	public void GetHeapType_GivenPrelude_DoesNotThrowException() {
		Prelude.getHeapType();
	}

	@Test
	public void GetHeapVariable_GivenPrelude_DoesNotThrowException() {
		Prelude.getHeapVariable();
	}

	@Test
	public void GetHeapAccessExpression_GivenPrelude_DoesNotThrowException() {
		final Expression base = new ValueReference(new Accessor("reference"));
		final Expression field = new ValueReference(new Accessor("Reference.field"));
		Prelude.getHeapAccessExpression(base, field);
	}

}
