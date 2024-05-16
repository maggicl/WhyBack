package byteback.whyml.vimp;

import byteback.analysis.TypeSwitch;
import byteback.whyml.identifiers.Identifier;
import byteback.whyml.syntax.type.WhyArrayType;
import byteback.whyml.syntax.type.WhyJVMType;
import byteback.whyml.syntax.type.WhyReference;
import byteback.whyml.syntax.type.WhyType;
import soot.ArrayType;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.LongType;
import soot.NullType;
import soot.RefType;
import soot.ShortType;
import soot.Type;
import soot.VoidType;

public class TypeAccessExtractor extends TypeSwitch<WhyType> {
	private final VimpClassNameParser classNameParser;
	private final boolean resolveRefType;
	private WhyType type;

	public TypeAccessExtractor(VimpClassNameParser classNameParser, boolean resolveRefType) {
		this.classNameParser = classNameParser;
		this.resolveRefType = resolveRefType;
	}

	@Override
	public void caseByteType(final ByteType byteType) {
		type = WhyJVMType.BYTE;
	}

	@Override
	public void caseShortType(final ShortType shortType) {
		type = WhyJVMType.SHORT;
	}

	@Override
	public void caseIntType(final IntType integerType) {
		type = WhyJVMType.INT;
	}

	@Override
	public void caseCharType(final CharType charType) {
		type = WhyJVMType.CHAR;
	}

	@Override
	public void caseLongType(final LongType longType) {
		type = WhyJVMType.LONG;
	}

	@Override
	public void caseDoubleType(final DoubleType doubleType) {
		type = WhyJVMType.DOUBLE;
	}

	@Override
	public void caseFloatType(final FloatType floatType) {
		type = WhyJVMType.FLOAT;
	}

	@Override
	public void caseBooleanType(final BooleanType booleanType) {
		type = WhyJVMType.BOOL;
	}

	@Override
	public void caseRefType(final RefType referenceType) {
		type = resolveRefType
				? new WhyReference(classNameParser.parse(referenceType.getSootClass()))
				: WhyJVMType.PTR;
	}

	@Override
	public void caseArrayType(final ArrayType arrayType) {
		if (resolveRefType) {
			// TODO: consider rewriting this
			final TypeAccessExtractor e = new TypeAccessExtractor(classNameParser, true);
			e.visit(arrayType.getElementType());
			type = new WhyArrayType(e.result());
		} else {
			type = WhyJVMType.PTR;
		}
	}

	@Override
	public void caseVoidType(VoidType t) {
		type = WhyJVMType.UNIT;
	}

	@Override
	public void caseNullType(NullType t) {
		type = new WhyReference(Identifier.Special.OBJECT);
	}

	@Override
	public void caseDefault(final Type type) {
		throw new IllegalStateException("Cannot extract type access for type " + type);
	}

	@Override
	public WhyType result() {
		if (type == null) {
			throw new IllegalStateException("Could not retrieve type access");
		}

		return type;
	}
}
