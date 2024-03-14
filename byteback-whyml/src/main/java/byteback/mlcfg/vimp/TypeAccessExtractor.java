package byteback.mlcfg.vimp;

import byteback.analysis.TypeSwitch;
import byteback.mlcfg.identifiers.FQDNEscaper;
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
	private final FQDNEscaper fqdnEscaper;

	private WhyType type;

	public TypeAccessExtractor(FQDNEscaper fqdnEscaper) {
		this.fqdnEscaper = fqdnEscaper;
	}

	@Override
	public void caseByteType(final ByteType byteType) {
		type = WhyPrimitive.BYTE;
	}

	@Override
	public void caseShortType(final ShortType shortType) {
		type = WhyPrimitive.SHORT;
	}

	@Override
	public void caseIntType(final IntType integerType) {
		type = WhyPrimitive.INT;
	}

	@Override
	public void caseCharType(final CharType charType) {
		type = WhyPrimitive.CHAR;
	}

	@Override
	public void caseLongType(final LongType longType) {
		type = WhyPrimitive.LONG;
	}

	@Override
	public void caseDoubleType(final DoubleType doubleType) {
		type = WhyPrimitive.DOUBLE;
	}

	@Override
	public void caseFloatType(final FloatType floatType) {
		type = WhyPrimitive.FLOAT;
	}

	@Override
	public void caseBooleanType(final BooleanType booleanType) {
		type = WhyPrimitive.BOOL;
	}

	@Override
	public void caseRefType(final RefType referenceType) {
		type = new WhyReference(fqdnEscaper.escape(referenceType.getClassName()));
	}

	@Override
	public void caseArrayType(final ArrayType arrayType) {
		// TODO: consider rewriting this
		final TypeAccessExtractor e = new TypeAccessExtractor(fqdnEscaper);
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
