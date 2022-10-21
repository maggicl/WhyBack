package byteback.vimp.transformer;

import soot.Body;
import soot.ValueBox;

public interface ValueTransformer {

	void transformValue(ValueBox vbox);

	default void internalTransform(final Body body) {
		for (final ValueBox vbox : body.getUseBoxes()) {
			transformValue(vbox);
		}
	}

}
