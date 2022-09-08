package byteback.core.representation.soot.body;

import byteback.core.representation.Visitor;
import soot.Value;
import soot.grimp.AbstractGrimpValueSwitch;
import soot.jimple.DynamicInvokeExpr;
import soot.jimple.InterfaceInvokeExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.StaticInvokeExpr;
import soot.jimple.VirtualInvokeExpr;

public abstract class SootExpressionVisitor<R> extends AbstractGrimpValueSwitch<R> implements Visitor<Value, R> {

	public void caseInvokeExpr(final InvokeExpr invoke) {
		caseDefault(invoke);
	}

	@Override
	public void caseStaticInvokeExpr(final StaticInvokeExpr invoke) {
		caseInvokeExpr(invoke);
	}

	@Override
	public void caseVirtualInvokeExpr(final VirtualInvokeExpr invoke) {
		caseInvokeExpr(invoke);
	}

	@Override
	public void caseInterfaceInvokeExpr(final InterfaceInvokeExpr invoke) {
		caseInvokeExpr(invoke);
	}

	@Override
	public void caseDynamicInvokeExpr(final DynamicInvokeExpr invoke) {
		caseInvokeExpr(invoke);
	}

	public void defaultCase(final Object object) {
		caseDefault((Value) object);
	}

	public R visit(final Value value) {
		value.apply(this);

		return result();
	}

}
