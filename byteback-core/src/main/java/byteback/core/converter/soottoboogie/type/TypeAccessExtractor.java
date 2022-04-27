package byteback.core.converter.soottoboogie.type;

import byteback.core.converter.soottoboogie.ConversionException;
import byteback.core.converter.soottoboogie.Prelude;
import byteback.core.representation.soot.type.SootTypeVisitor;
import byteback.frontend.boogie.ast.TypeAccess;
import soot.ArrayType;
import soot.BooleanType;
import soot.ByteType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.LongType;
import soot.RefType;
import soot.ShortType;
import soot.Type;

public class TypeAccessExtractor extends SootTypeVisitor<TypeAccess> {

	private TypeAccess typeAccess;

	public void setTypeAccess(final TypeAccess typeAccess) {
		this.typeAccess = typeAccess;
	}

	@Override
	public void caseByteType(final ByteType byteType) {
		setTypeAccess(Prelude.instance().getIntegerType().makeTypeAccess());
	}

	@Override
	public void caseShortType(final ShortType shortType) {
		setTypeAccess(Prelude.instance().getIntegerType().makeTypeAccess());
	}

	@Override
	public void caseIntType(final IntType integerType) {
		setTypeAccess(Prelude.instance().getIntegerType().makeTypeAccess());
	}

	@Override
	public void caseLongType(final LongType longType) {
		setTypeAccess(Prelude.instance().getIntegerType().makeTypeAccess());
	}

	@Override
	public void caseDoubleType(final DoubleType doubleType) {
		setTypeAccess(Prelude.instance().getRealType().makeTypeAccess());
	}

	@Override
	public void caseFloatType(final FloatType floatType) {
		setTypeAccess(Prelude.instance().getRealType().makeTypeAccess());
	}

	@Override
	public void caseBooleanType(final BooleanType booleanType) {
		setTypeAccess(Prelude.instance().getBooleanType().makeTypeAccess());
	}

	@Override
	public void caseRefType(final RefType referenceType) {
		setTypeAccess(Prelude.instance().getReferenceType().makeTypeAccess());
	}

	@Override
	public void caseArrayType(final ArrayType arrayType) {
		setTypeAccess(Prelude.instance().getReferenceType().makeTypeAccess());
	}

	@Override
	public void caseDefault(final Type type) {
		throw new ConversionException("Cannot extract type access for type " + type);
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
