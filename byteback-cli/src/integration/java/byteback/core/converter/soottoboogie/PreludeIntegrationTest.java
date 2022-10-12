package byteback.core.converter.soottoboogie;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import byteback.frontend.boogie.ast.*;
import org.junit.BeforeClass;
import org.junit.Test;

public class PreludeIntegrationTest {

	@BeforeClass
	public static void before() {
		Prelude.v().loadDefault();
	}

	@Test
	public void LoadProgram_CalledTwice_ReturnsTheSameInstance() {

		final Program a = Prelude.v().program();
		final Program b = Prelude.v().program();
		assertEquals(b, a);
	}

	@Test
	public void GetReferenceType_GivenDefaultPrelude_ReturnsExpectedType() {
		final Type type = Prelude.v().getReferenceType();
		assertEquals("Reference", type.makeTypeAccess().getIdentifier());
	}

	@Test
	public void GetFieldType_GivenDefaultPrelude_ReturnsExpectedType() {
		final Type type = Prelude.v().getFieldType();
		assertEquals("Field", type.makeTypeAccess().getIdentifier());
	}

	@Test
	public void GetNullConstant_GivenDefaultPrelude_ReturnsExpectedVariable() {
		final Variable variable = Prelude.v().getNullConstant();
		assertEquals("~null", variable.getName());
	}

	@Test
	public void GetHeapAccessFunction_GivenDefaultPrelude_ReturnsExpectedFunction() {
		final Function function = Prelude.v().getHeapAccessFunction();
		assertEquals("~heap.read", function.getName());
	}

	@Test
	public void GetHeapUpdateProcedure_GivenDefaultPrelude_ReturnsExpectedFunction() {
		final Function function = Prelude.v().getHeapUpdateFunction();
		assertEquals("~heap.update", function.getName());
	}

	@Test
	public void GetNewProcedure_GivenDefaultPrelude_ReturnsExpectedProcedure() {
		final Procedure procedure = Prelude.v().getNewProcedure();
		assertEquals("~new", procedure.getName());
	}

	@Test
	public void GetArrayProcedure_GivenDefaultPrelude_ReturnsExpectedProcedure() {
		final Procedure procedure = Prelude.v().getArrayProcedure();
		assertEquals("~array", procedure.getName());
	}

	@Test
	public void GetIntCastingFunction_GivenDefaultPrelude_ReturnsExpectedFunction() {
		final Function function = Prelude.v().getIntCastingFunction();
		assertEquals("~int", function.getName());
	}

	@Test
	public void GetIntToRealCastingFunction_GivenDefaultPrelude_ReturnsExpectedFunction() {
		final Function function = Prelude.v().getIntToRealCastingFunction();
		assertEquals("~int_to_real", function.getName());
	}

	@Test
	public void GetRealToIntCastingFunction_GivenDefaultPrelude_ReturnsExpectedFunction() {
		final Function function = Prelude.v().getRealToIntCastingFunction();
		assertEquals("~real_to_int", function.getName());
	}

	@Test
	public void GetCmpFunction_GivenDefaultPrelude_ReturnsExpectedFunction() {
		final Function function = Prelude.v().getCmpFunction();
		assertEquals("~cmp", function.getName());
	}

}
