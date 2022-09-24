package byteback;

import byteback.core.util.Lazy;
import soot.Type;

public class LogicType extends Type {

	private static final Lazy<LogicType> instance = Lazy.from(() -> new LogicType());

	public static final int HASHCODE = 0x1C4585DB;

	public static LogicType v() {
		return instance.get();
	}

	@Override
	public boolean isAllowedInFinalCode() {
		return false;
	}

	@Override
	public String toString() {
		return "logic";
	}

  @Override
  public int hashCode() {
    return HASHCODE;
  }

}
