package byteback.whyml.syntax.statement;

import byteback.whyml.identifiers.Identifier;
import byteback.whyml.printer.Code;
import static byteback.whyml.printer.SExpr.prefix;
import static byteback.whyml.printer.SExpr.terminal;
import byteback.whyml.syntax.expr.Expression;
import byteback.whyml.syntax.statement.visitor.StatementVisitor;
import byteback.whyml.syntax.type.WhyJVMType;
import java.util.Set;

/**
 * @param elementType We need to know the element type of the array as the array expression only has a JVM type of "ref" no matter what
 */
public record ArrayAssignment(Expression base,
							  WhyJVMType elementType,
							  Expression index,
							  Expression value) implements CFGStatement {
	public ArrayAssignment {
		if (base.type() != WhyJVMType.PTR) {
			throw new IllegalArgumentException("base of an array expression must be of type PTR (i.e. an array)");
		}

		final WhyJVMType valueType = value.type();

		if (elementType != valueType) {
			throw new IllegalArgumentException("cannot assign to array with type %s an expression with type %s"
					.formatted(elementType, valueType));
		}
	}

	@Override
	public Code toWhy() {
		final String accessor = "R" + elementType.getWhyAccessorScope();

		return prefix(
				accessor + ".store",
				terminal(Identifier.Special.HEAP),
				base.toWhy(),
				index.toWhy(),
				value.toWhy()
		).statement("", ";");
	}

	@Override
	public void accept(StatementVisitor visitor) {
		visitor.visitArrayAssignmentStatement(this);
	}
}
