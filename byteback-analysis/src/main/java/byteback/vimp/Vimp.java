package byteback.vimp;

import byteback.core.util.Lazy;
import byteback.vimp.internal.AssertionStmt;
import byteback.vimp.internal.AssumptionStmt;
import byteback.vimp.internal.InvariantStmt;
import byteback.vimp.internal.LogicAndExpr;
import byteback.vimp.internal.LogicExistsExpr;
import byteback.vimp.internal.LogicForallExpr;
import byteback.vimp.internal.LogicIffExpr;
import byteback.vimp.internal.LogicImpliesExpr;
import byteback.vimp.internal.LogicNotExpr;
import byteback.vimp.internal.LogicOrExpr;
import soot.Body;
import soot.Local;
import soot.SootMethod;
import soot.Value;
import soot.ValueBox;
import soot.grimp.internal.ExprBox;
import soot.jimple.Constant;
import soot.util.Chain;

public class Vimp {

	private static final Lazy<Vimp> instance = Lazy.from(() -> new Vimp());

	public static Vimp v() {
		return instance.get();
	}

	public ValueBox newArgBox(final Value value) {
		return new ExprBox(value);
	}

  public static Value cloneIfNecessary(final Value v) {
    if (v instanceof Local || v instanceof Constant) {
      return v;
    } else {
      return (Value) v.clone();
    }
  }

	public Body newBody(final SootMethod method) {
		return new VimpBody(method);
	}

	public AssertionStmt newAssertionStmt(final Value c) {
		return new AssertionStmt(c);
	}

	public AssumptionStmt newAssumptionStmt(final Value c) {
		return new AssumptionStmt(c);
	}

	public InvariantStmt newInvariantStmt(final Value c) {
		return new InvariantStmt(c);
	}

	public LogicAndExpr newLogicAndExpr(final Value a, final Value b)  {
		return new LogicAndExpr(a, b);
	}

	public LogicAndExpr newLogicAndExpr(final ValueBox abox, final ValueBox bbox)  {
		return new LogicAndExpr(abox, bbox);
	}

	public LogicOrExpr newLogicOrExpr(final Value a, final Value b)  {
		return new LogicOrExpr(a, b);
	}

	public LogicOrExpr newLogicOrExpr(final ValueBox abox, final ValueBox bbox)  {
		return new LogicOrExpr(abox, bbox);
	}

	public LogicIffExpr newLogicIffExpr(final Value a, final Value b)  {
		return new LogicIffExpr(a, b);
	}

	public LogicNotExpr newLogicNotExpr(final Value v) {
		return new LogicNotExpr(v);
	}

	public LogicNotExpr newLogicNotExpr(final ValueBox vbox) {
		return new LogicNotExpr(vbox);
	}

	public LogicImpliesExpr newLogicImpliesExpr(final Value a, final Value b)  {
		return new LogicImpliesExpr(a, b);
	}

	public LogicForallExpr newLogicForallExpr(final Chain<Local> ls, final Value v)  {
		return new LogicForallExpr(ls, v);
	}

	public LogicExistsExpr newLogicExistsExpr(final Chain<Local> ls, final Value v)  {
		return new LogicExistsExpr(ls, v);
	}
	
}
