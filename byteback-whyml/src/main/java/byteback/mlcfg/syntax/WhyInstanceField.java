package byteback.mlcfg.syntax;

import byteback.mlcfg.identifiers.Identifier;
import byteback.mlcfg.syntax.types.WhyType;

public class WhyInstanceField extends WhyField {
	public WhyInstanceField(Identifier.FQDN clazz, Identifier.U name, WhyType type) {
		super(clazz, name, type);
	}

	@Override
	public boolean isStatic() {
		return false;
	}
}
