package byteback.vimp.transformer;

import soot.Unit;
import soot.UnitBox;

public interface StatementTransformer {

	default void putStatement(final UnitBox ubox, final Unit newUnit) {
		final Unit oldUnit = ubox.getUnit();
		ubox.setUnit(newUnit);
		oldUnit.redirectJumpsToThisTo(newUnit);
	}

}
