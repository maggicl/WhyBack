package byteback.whyml.syntax.function;

import byteback.whyml.printer.Code;
import static byteback.whyml.printer.Code.indent;
import static byteback.whyml.printer.Code.line;
import static byteback.whyml.printer.Code.many;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.statement.visitor.SideEffectVisitor;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public sealed abstract class WhyFunctionBody {
	private volatile WhySideEffects sideEffects = null;

	protected abstract void visit(SideEffectVisitor v);

	public abstract Set<WhyFunctionDeclaration> forDecls();

	public WhySideEffects sideEffects() {
		if (sideEffects == null) {
			synchronized (this) {
				if (sideEffects == null) {
					final SideEffectVisitor v = new SideEffectVisitor();
					visit(v);
					sideEffects = v.sideEffects();
				}
			}
		}

		return sideEffects;
	}

	public abstract Code toWhy();

	public static final class SpecBody extends WhyFunctionBody {
		private final Expression expression;

		public SpecBody(Expression expression) {
			this.expression = expression;
		}

		public Expression getExpression() {
			return expression;
		}

		@Override
		protected void visit(SideEffectVisitor v) {
			expression.accept(v);
		}

		@Override
		public Set<WhyFunctionDeclaration> forDecls() {
			return EnumSet.of(WhyFunctionDeclaration.FUNCTION, WhyFunctionDeclaration.PREDICATE);
		}

		@Override
		public Code toWhy() {
			return expression.toWhy().statement("= ", "");
		}
	}

	public static final class CFGBody extends WhyFunctionBody {
		private final List<WhyLocal> locals;
		private final List<CFGBlock> blocks;

		public CFGBody(List<WhyLocal> locals, List<CFGBlock> blocks) {
			this.locals = locals;
			this.blocks = blocks;
		}

		public List<WhyLocal> locals() {
			return locals;
		}

		public List<CFGBlock> blocks() {
			return blocks;
		}

		@Override
		protected void visit(SideEffectVisitor v) {
			for (final CFGBlock b : blocks) {
				b.allStatements().forEach(e -> e.accept(v));
			}
		}

		@Override
		public Set<WhyFunctionDeclaration> forDecls() {
			return EnumSet.of(WhyFunctionDeclaration.PROGRAM);
		}

		@Override
		public Code toWhy() {
			return many(
					line("="),
					indent(
							many(locals.stream().map(WhyLocal::toWhy)),
							line("{"),
							indent(
									many(locals.stream().map(WhyLocal::initialization)),
									line("goto %s".formatted(blocks.get(0).label().name()))
							),
							line("}"),
							many(blocks.stream().map(CFGBlock::toWhy))
					)
			);
		}
	}
}
