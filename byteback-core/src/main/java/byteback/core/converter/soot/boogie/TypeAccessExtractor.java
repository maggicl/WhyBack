package byteback.core.converter.soot.boogie;

import byteback.core.representation.soot.type.SootTypeVisitor;
import byteback.frontend.boogie.ast.TypeAccess;
import soot.BooleanType;
import soot.ByteType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.RefType;
import soot.Type;

public class TypeAccessExtractor extends SootTypeVisitor<TypeAccess> {

	private TypeAccess typeAccess;

	public void setTypeAccess(final TypeAccess typeAccess) {
		this.typeAccess = typeAccess;
	}

	@Override
	public void caseByteType(final ByteType byteType) {
		setTypeAccess(Prelude.getIntegerType().getTypeAccess());
	}

	@Override
	public void caseIntType(final IntType integerType) {
		setTypeAccess(Prelude.getIntegerType().getTypeAccess());
	}

	@Override
	public void caseDoubleType(final DoubleType doubleType) {
		setTypeAccess(Prelude.getRealType().getTypeAccess());
	}

	@Override
	public void caseFloatType(final FloatType floatType) {
		setTypeAccess(Prelude.getRealType().getTypeAccess());
	}

	@Override
	public void caseBooleanType(final BooleanType booleanType) {
		setTypeAccess(Prelude.getBooleanType().getTypeAccess());
	}

	@Override
	public void caseRefType(final RefType referenceType) {
		setTypeAccess(Prelude.getReferenceType().getTypeAccess());
	}

	@Override
	public void caseDefault(final Type type) {
		throw new UnsupportedOperationException("Cannot extract type access for Soot type " + type);
	}

	@Override
	public TypeAccess result() {
		if (typeAccess == null) {
			throw new IllegalStateException("Could not retrieve type access");
		} else {
			return typeAccess;
		}
	}

}
