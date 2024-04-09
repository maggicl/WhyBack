package byteback.whyml.syntax.field;

import byteback.whyml.identifiers.Identifier;
import byteback.whyml.syntax.type.WhyType;

public class WhyStaticField extends WhyField {
	public WhyStaticField(Identifier.FQDN clazz, Identifier.U name, WhyType type) {
		super(clazz, name, type);
	}

	@Override
	public boolean isStatic() {
		return true;
	}
}
