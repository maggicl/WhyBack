package byteback.whyml.syntax.field;

import byteback.whyml.identifiers.Identifier;
import byteback.whyml.syntax.type.WhyType;

public abstract class WhyField {
	private final Identifier.FQDN clazz;
	private final Identifier.U name;
	private final WhyType type;

	@Override
	public String toString() {
		return "WhyField[clazz=%s, name=%s, type=%s, isStatic=%s]".formatted(clazz, name, type, isStatic());
	}

	protected WhyField(Identifier.FQDN clazz, Identifier.U name, WhyType type) {
		this.clazz = clazz;
		this.name = name;
		this.type = type;
	}

	public abstract boolean isStatic();

	public Identifier.U getName() {
		return name;
	}

	public WhyType getType() {
		return type;
	}

	public Identifier.FQDN getClazz() {
		return clazz;
	}
}

