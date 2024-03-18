package byteback.mlcfg.syntax.spec;

public sealed abstract class SpecExpression permits DoubleLiteral, FloatLiteral {
	public abstract String toWhy();
}
