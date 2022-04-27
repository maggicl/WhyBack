package byteback.core.converter.soottoboogie;

import static org.junit.Assert.assertEquals;

import byteback.frontend.boogie.ast.*;
import org.junit.Test;

public class PreludeIntegrationTest {

	@Test
	public void InitializeProgram_GivenUninitializedPrelude_DoesNotThrowExceptions() {
		Prelude.initializeProgram();
	}

	@Test
	public void LoadProgram_CalledTwice_ReturnsTheSameInstance() {
		final Program a = Prelude.loadProgram();
		final Program b = Prelude.loadProgram();
		assertEquals(b, a);
	}

	@Test
	public void GetReferenceType_GivenDefaultPrelude_ReturnsExpectedType() {
		final Type type = Prelude.getReferenceType();
		assertEquals("Reference", type.makeTypeAccess().getIdentifier());
	}


	@Test
	public void GetFieldType_GivenDefaultPrelude_ReturnsExpectedType() {
		final Type type = Prelude.getFieldType();
		assertEquals("Field", type.makeTypeAccess().getIdentifier());
	}

	@Test
	public void GetNullConstant_GivenDefaultPrelude_ReturnsExpectedVariable() {
		final Variable variable = Prelude.getNullConstant();
		assertEquals("~null", variable.getName());
		assertEquals("Reference", variable.getTypeAccess().getIdentifier());
	}

	@Test
	public void GetHeapAccessFunction_GivenDefaultPrelude_ReturnsExpectedFunction() {
		final Function function = Prelude.getHeapAccessFunction();
		assertEquals("~read", function.getName());
	}

	@Test
	public void GetHeapUpdateProcedure_GivenDefaultPrelude_ReturnsExpectedFunction() {
		final Procedure procedure = Prelude.getHeapUpdateProcedure();
		assertEquals("~update", procedure.getName());
	}

	@Test
	public void GetNewProcedure_GivenDefaultPrelude_ReturnsExpectedProcedure() {
		final Procedure procedure = Prelude.getNewProcedure();
		assertEquals("~new", procedure.getName());
	}

	@Test
	public void GetArrayProcedure_GivenDefaultPrelude_ReturnsExpectedProcedure() {
		final Procedure procedure = Prelude.getArrayProcedure();
		assertEquals("~array", procedure.getName());
	}

	@Test
	public void GetArrayAccessFunction_GivenDefaultPrelude_ReturnsExpectedFunction() {
		final Function function = Prelude.getArrayAccessFunction();
		assertEquals("~get", function.getName());
	}

	@Test
	public void GetArrayUpdateFunction_GivenDefaultPrelude_ReturnsExpectedFunction() {
		final Procedure procedure = Prelude.getArrayUpdateProcedure();
		assertEquals("~insert", procedure.getName());
	}

	@Test
	public void GetIntCastingFunction_GivenDefaultPrelude_ReturnsExpectedFunction() {
		final Function function = Prelude.getIntCastingFunction();
		assertEquals("~int", function.getName());
	}

	@Test
	public void GetCmpFunction_GivenDefaultPrelude_ReturnsExpectedFunction() {
		final Function function = Prelude.getCmpFunction();
		assertEquals("~cmp", function.getName());
	}

}
