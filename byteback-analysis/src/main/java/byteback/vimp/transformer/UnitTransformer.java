package byteback.vimp.transformer;

import soot.Body;
import soot.Unit;
import soot.UnitBox;

public interface UnitTransformer {

	public static void putStatement(final UnitBox ubox, final Unit newUnit) {
		final Unit oldUnit = ubox.getUnit();
		ubox.setUnit(newUnit);
		oldUnit.redirectJumpsToThisTo(newUnit);
	}

	void transformUnit(UnitBox ubox);

	default void internalTransform(final Body body) {
		for (final UnitBox ubox : body.getAllUnitBoxes()) {
			transformUnit(ubox);
		}
	}

}
