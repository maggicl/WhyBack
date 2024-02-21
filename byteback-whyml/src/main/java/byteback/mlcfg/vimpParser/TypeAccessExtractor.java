package byteback.mlcfg.vimpParser;

import byteback.analysis.TypeSwitch;
import byteback.mlcfg.syntax.identifiers.IdentifierEscaper;
import byteback.mlcfg.syntax.types.WhyArrayType;
import byteback.mlcfg.syntax.types.WhyPrimitive;
import byteback.mlcfg.syntax.types.WhyReference;
import byteback.mlcfg.syntax.types.WhyType;
import soot.ArrayType;
import soot.BooleanType;
import soot.ByteType;
import soot.CharType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.LongType;
import soot.RefType;
import soot.ShortType;
import soot.Type;

public class TypeAccessExtractor extends TypeSwitch<WhyType> {

	private final IdentifierEscaper escaper;

	private WhyType type;

	public TypeAccessExtractor(IdentifierEscaper escaper) {
		this.escaper = escaper;
	}

	@Override
	public void caseByteType(final ByteType byteType) {
		type = WhyPrimitive.INT_INT;
	}

	@Override
	public void caseShortType(final ShortType shortType) {
		type = WhyPrimitive.INT_INT;
	}

	@Override
	public void caseIntType(final IntType integerType) {
		type = WhyPrimitive.INT_INT;
	}

	@Override
	public void caseCharType(final CharType charType) {
		type = WhyPrimitive.INT_INT;
	}

	@Override
	public void caseLongType(final LongType longType) {
		type = WhyPrimitive.INT_INT;
	}

	@Override
	public void caseDoubleType(final DoubleType doubleType) {
		type = WhyPrimitive.FLOAT_11_52;
	}

	@Override
	public void caseFloatType(final FloatType floatType) {
		type = WhyPrimitive.FLOAT_8_23;
	}

	@Override
	public void caseBooleanType(final BooleanType booleanType) {
		type = WhyPrimitive.BOOL;
	}

	@Override
	public void caseRefType(final RefType referenceType) {
		type = new WhyReference(escaper.escape(referenceType.getClassName()));
	}

	@Override
	public void caseArrayType(final ArrayType arrayType) {
		// TODO: consider rewriting this
		final TypeAccessExtractor e = new TypeAccessExtractor(escaper);
		e.visit(arrayType.getElementType());
		type = new WhyArrayType(e.result());
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
