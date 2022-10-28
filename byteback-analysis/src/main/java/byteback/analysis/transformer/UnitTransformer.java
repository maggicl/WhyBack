package byteback.analysis.transformer;

import byteback.analysis.util.SootBodies;
import soot.Body;
import soot.Unit;
import soot.UnitBox;

public interface UnitTransformer {

	public static void putStatement(final UnitBox unitBox, final Unit newUnit) {
		unitBox.setUnit(newUnit);
	}

	void transformUnit(UnitBox unitBox);

	default void transformBody(final Body body) {
		for (UnitBox unitBox : SootBodies.getUnitBoxes(body)) {
			transformUnit(unitBox);
		}
	}

}
