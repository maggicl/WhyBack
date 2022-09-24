package byteback.vimp;

import byteback.core.util.Lazy;
import soot.Local;
import soot.Value;
import soot.ValueBox;
import soot.grimp.internal.ExprBox;
import soot.jimple.Constant;

public class Vimp {

	private static final Lazy<Vimp> instance = Lazy.from(() -> new Vimp());

	public static Vimp v() {
		return instance.get();
	}

	public ValueBox newArgBox(final Value value) {
		return new ExprBox(value);
	}

  public static Value cloneIfNecessary(final Value val) {
    if (val instanceof Local || val instanceof Constant) {
      return val;
    } else {
      return (Value) val.clone();
    }
  }
	
}
