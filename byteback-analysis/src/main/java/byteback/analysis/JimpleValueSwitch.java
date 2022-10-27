package byteback.analysis;

import soot.Value;
import soot.jimple.*;

public abstract class JimpleValueSwitch<R> extends AbstractJimpleValueSwitch<R>
		implements
			LogicExprSwitch,
			Visitor<Value, R> {

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

	@Override
	public void caseSpecialInvokeExpr(final SpecialInvokeExpr invoke) {
		caseInvokeExpr(invoke);
	}

	public void caseInvokeExpr(final InvokeExpr invoke) {
		defaultCase(invoke);
	}

	@Override
	public void caseDefault(final Value value) {
	}

	public R visit(final Value value) {
		value.apply(this);

		return result();
	}

}
