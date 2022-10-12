package byteback.vimp;

import byteback.core.util.Lazy;
import byteback.vimp.internal.LogicAndExpr;
import byteback.vimp.internal.LogicExistsExpr;
import byteback.vimp.internal.LogicForallExpr;
import byteback.vimp.internal.LogicIffExpr;
import byteback.vimp.internal.LogicImpliesExpr;
import byteback.vimp.internal.LogicOrExpr;
import soot.Local;
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

	public LogicAndExpr newLogicAndExpr(final Value a, final Value b)  {
		return new LogicAndExpr(a, b);
	}

	public LogicOrExpr newLogicOrExpr(final Value a, final Value b)  {
		return new LogicOrExpr(a, b);
	}

	public LogicIffExpr newLogicIffExpr(final Value a, final Value b)  {
		return new LogicIffExpr(a, b);
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
