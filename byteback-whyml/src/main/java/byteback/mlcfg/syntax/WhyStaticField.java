package byteback.mlcfg.syntax;

import byteback.mlcfg.identifiers.Identifier;
import byteback.mlcfg.syntax.types.WhyType;

public class WhyStaticField extends WhyField {
	public WhyStaticField(Identifier.FQDN clazz, Identifier.U name, WhyType type) {
		super(clazz, name, type);
	}

	@Override
	public boolean isStatic() {
		return true;
	}
}
