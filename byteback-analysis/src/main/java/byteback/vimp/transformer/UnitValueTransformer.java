package byteback.vimp.transformer;

import soot.Body;
import soot.UnitBox;
import soot.ValueBox;

public interface UnitValueTransformer extends UnitTransformer, ValueTransformer {

	@Override
	default void internalTransform(final Body body) {
		for (final UnitBox ubox : body.getAllUnitBoxes()) {
			transformUnit(ubox);

			for (final ValueBox vbox : ubox.getUnit().getUseBoxes()) {
				transformValue(vbox);
			}
		}
	}

}
