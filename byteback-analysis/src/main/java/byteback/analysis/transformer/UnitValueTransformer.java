package byteback.analysis.transformer;

import soot.Body;
import soot.UnitBox;
import soot.ValueBox;
import byteback.analysis.util.SootBodies;

public interface UnitValueTransformer extends UnitTransformer, ValueTransformer {

	@Override
	default void transformBody(final Body body) {

		for (final UnitBox unitBox : SootBodies.getUnitBoxes(body)) {
			for (final ValueBox valueBox : unitBox.getUnit().getUseBoxes()) {
				transformValue(valueBox);
			}

			transformUnit(unitBox);
		}
	}

}
