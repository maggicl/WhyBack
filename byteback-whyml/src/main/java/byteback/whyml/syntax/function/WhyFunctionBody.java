package byteback.whyml.syntax.function;

import byteback.whyml.printer.Code;
import static byteback.whyml.printer.Code.indent;
import static byteback.whyml.printer.Code.line;
import static byteback.whyml.printer.Code.many;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.expr.transformer.CallDependenceVisitor;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

public sealed abstract class WhyFunctionBody {
	public abstract Set<WhyFunctionDeclaration> forDecls();

	public abstract Set<WhyFunctionSignature> getCallees();

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
		public Set<WhyFunctionDeclaration> forDecls() {
			return EnumSet.of(WhyFunctionDeclaration.FUNCTION, WhyFunctionDeclaration.PREDICATE);
		}

		@Override
		public Set<WhyFunctionSignature> getCallees() {
			return CallDependenceVisitor.getCallees(expression);
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
		public Set<WhyFunctionDeclaration> forDecls() {
			return EnumSet.of(WhyFunctionDeclaration.PROGRAM);
		}

		@Override
		public Set<WhyFunctionSignature> getCallees() {
			// TODO: change
			return Set.of();
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
