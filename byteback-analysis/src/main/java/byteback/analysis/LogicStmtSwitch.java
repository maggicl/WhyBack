package byteback.analysis;

import soot.Unit;
import soot.jimple.StmtSwitch;

public interface LogicStmtSwitch extends StmtSwitch, LogicStmtVisitor {

	@Override
	default void defaultCase(final Object object) {
		defaultCase((Unit) object);
	}

}
