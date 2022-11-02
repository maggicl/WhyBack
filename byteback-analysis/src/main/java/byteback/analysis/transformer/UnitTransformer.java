package byteback.analysis.transformer;

import java.util.Iterator;

import byteback.analysis.SwappableUnitBox;
import soot.Body;
import soot.Unit;
import soot.UnitBox;

public interface UnitTransformer {

	void transformUnit(UnitBox unitBox);

	default void transformBody(final Body body) {

		final Iterator<Unit> iterator = body.getUnits().snapshotIterator();

		while (iterator.hasNext()) {
			final Unit unit = iterator.next();

			transformUnit(new SwappableUnitBox(unit, body));
		}
	}

}
