package byteback.whyml.syntax.type;

import byteback.whyml.identifiers.Identifier;
import byteback.whyml.printer.SExpr;
import static byteback.whyml.printer.SExpr.prefix;
import static byteback.whyml.printer.SExpr.terminal;

public record WhyReference(Identifier.FQDN fqdn) implements WhyPtrType {
	public static final WhyReference OBJECT = new WhyReference(Identifier.Special.OBJECT);

	@Override
	public String getWhyAccessorScope() {
		return "L";
	}

	@Override
	public String getDescriptor() {
		return "L" + fqdn.descriptor();
	}

	public SExpr getPreludeClassType() {
		return terminal("%s.class".formatted(fqdn));
	}

	@Override
	public SExpr getPreludeType() {
		// we can't use the full name if we are in a scope that matches it
		return prefix("Class", getPreludeClassType());
	}

	@Override
	public void accept(WhyTypeVisitor visitor) {
		visitor.visitReference(this);
	}
}
