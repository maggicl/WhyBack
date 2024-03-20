package byteback.mlcfg.syntax.expr;

import byteback.mlcfg.syntax.types.WhyType;

public interface Expression {
	String toWhy();

	WhyType type();
}
