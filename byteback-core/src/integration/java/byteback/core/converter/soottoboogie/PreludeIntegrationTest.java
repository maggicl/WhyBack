package byteback.core.converter.soottoboogie;

import static org.junit.Assert.assertEquals;

import byteback.frontend.boogie.ast.*;
import org.junit.BeforeClass;
import org.junit.Test;

public class PreludeIntegrationTest {

	@BeforeClass
	public static void before() {
		Prelude.instance().loadDefault();
	}

	@Test
	public void LoadProgram_CalledTwice_ReturnsTheSameInstance() {

		final Program a = Prelude.instance().program();
		final Program b = Prelude.instance().program();
		assertEquals(b, a);
	}

	@Test
	public void GetReferenceType_GivenDefaultPrelude_ReturnsExpectedType() {
		final Type type = Prelude.instance().getReferenceType();
		assertEquals("Reference", type.makeTypeAccess().getIdentifier());
	}

	@Test
	public void GetFieldType_GivenDefaultPrelude_ReturnsExpectedType() {
		final Type type = Prelude.instance().getFieldType();
		assertEquals("Field", type.makeTypeAccess().getIdentifier());
	}

	@Test
	public void GetNullConstant_GivenDefaultPrelude_ReturnsExpectedVariable() {
		final Variable variable = Prelude.instance().getNullConstant();
		assertEquals("~null", variable.getName());
	}

	@Test
	public void GetHeapAccessFunction_GivenDefaultPrelude_ReturnsExpectedFunction() {
		final Function function = Prelude.instance().getHeapAccessFunction();
		assertEquals("~heap.read", function.getName());
	}

	@Test
	public void GetHeapUpdateProcedure_GivenDefaultPrelude_ReturnsExpectedFunction() {
		final Function function = Prelude.instance().getHeapUpdateFunction();
		assertEquals("~heap.update", function.getName());
	}

	@Test
	public void GetNewProcedure_GivenDefaultPrelude_ReturnsExpectedProcedure() {
		final Procedure procedure = Prelude.instance().getNewProcedure();
		assertEquals("~new", procedure.getName());
	}

	@Test
	public void GetArrayProcedure_GivenDefaultPrelude_ReturnsExpectedProcedure() {
		final Procedure procedure = Prelude.instance().getArrayProcedure();
		assertEquals("~array", procedure.getName());
	}

	@Test
	public void GetBoxFunction_GivenDefaultPrelude_ReturnsExpectedFunction() {
		final Function function = Prelude.instance().getBoxFunction();
		assertEquals("~box", function.getName());
	}

	@Test
	public void GetUnboxFunction_GivenDefaultPrelude_ReturnsExpectedFunction() {
		final Function function = Prelude.instance().getUnboxFunction();
		assertEquals("~unbox", function.getName());
	}

	@Test
	public void GetIntCastingFunction_GivenDefaultPrelude_ReturnsExpectedFunction() {
		final Function function = Prelude.instance().getIntCastingFunction();
		assertEquals("~int", function.getName());
	}

	@Test
	public void GetCmpFunction_GivenDefaultPrelude_ReturnsExpectedFunction() {
		final Function function = Prelude.instance().getCmpFunction();
		assertEquals("~cmp", function.getName());
	}

}
