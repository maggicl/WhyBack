package byteback.whyml.syntax.field;

import byteback.whyml.identifiers.Identifier;
import byteback.whyml.syntax.type.WhyType;

public class WhyInstanceField extends WhyField {
	public WhyInstanceField(Identifier.FQDN clazz, Identifier.L name, WhyType type) {
		super(clazz, name, type);
	}

	@Override
	public boolean isStatic() {
		return false;
	}
}
