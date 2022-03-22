package byteback.core.converter.soot.boogie;

import byteback.core.representation.soot.type.SootTypeVisitor;
import byteback.frontend.boogie.ast.TypeAccess;
import soot.BooleanType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.RefType;
import soot.Type;

public class BoogieTypeAccessExtractor extends SootTypeVisitor<TypeAccess> {

	private TypeAccess typeAccess;

	public void setTypeAccess(final TypeAccess typeAccess) {
		this.typeAccess = typeAccess;
	}

	@Override
	public void caseIntType(final IntType integerType) {
		setTypeAccess(BoogiePrelude.getIntegerType().getTypeAccess());
	}

	@Override
	public void caseDoubleType(final DoubleType doubleType) {
		setTypeAccess(BoogiePrelude.getRealType().getTypeAccess());
	}

	@Override
	public void caseFloatType(final FloatType floatType) {
		setTypeAccess(BoogiePrelude.getRealType().getTypeAccess());
	}

	@Override
	public void caseBooleanType(final BooleanType booleanType) {
		setTypeAccess(BoogiePrelude.getBooleanType().getTypeAccess());
	}

	@Override
	public void caseRefType(final RefType referenceType) {
		setTypeAccess(BoogiePrelude.getReferenceType().getTypeAccess());
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
