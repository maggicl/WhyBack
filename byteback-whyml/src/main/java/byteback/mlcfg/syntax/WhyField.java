package byteback.mlcfg.syntax;

import byteback.mlcfg.identifiers.Identifier;
import byteback.mlcfg.syntax.types.WhyType;

public abstract class WhyField {
	private final Identifier.FQDN clazz;
	private final Identifier.U name;
	private final WhyType type;

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

