package byteback.mlcfg.syntax.types;

public final class WhyUnitType implements WhyType {
	public static final WhyUnitType INSTANCE = new WhyUnitType();

	private WhyUnitType() {
	}

	@Override
	public String getWhyType() {
		return "unit";
	}

	@Override
	public String getPreludeType() {
		throw new UnsupportedOperationException("cannot use unit type in an expression type");
	}

	@Override
	public void accept(WhyTypeVisitor visitor) {
		visitor.visitUnit();
	}

	@Override
	public String getWhyAccessorScope() {
		throw new UnsupportedOperationException("cannot perform read/write operations on a unit type");
	}
}
