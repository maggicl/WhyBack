package byteback.analysis.transformer;

import byteback.util.Lazy;

import java.util.Iterator;
import java.util.Map;

import soot.ArrayType;
import soot.Body;
import soot.BodyTransformer;
import soot.Local;
import soot.LocalGenerator;
import soot.RefType;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.javaToJimple.DefaultLocalGenerator;
import soot.jimple.StringConstant;
import soot.util.Chain;

public class StringTransformer extends BodyTransformer {

	private static final Lazy<StringTransformer> instance = Lazy.from(StringTransformer::new);

	public static StringTransformer v() {
		return instance.get();
	}

	private StringTransformer() {
	}

	public Value expandStringConstant(final StringConstant stringConst, final Unit initUnit, final LocalGenerator localGenerator) {
		final Local arrayLocal = localGenerator.generateLocal(ArrayType.v(null, 0));
		final Local stringLocal = localGenerator.generateLocal(RefType.v("java.lang.String"));
		return null;
	}

	@Override
	public void internalTransform(final Body body, final String phaseName, final Map<String, String> options) {
		final Chain<Unit> units = body.getUnits();
		final Iterator<Unit> unitIterator = units.snapshotIterator();
		final LocalGenerator localGenerator = new DefaultLocalGenerator(body);
		final Unit initUnit = units.getFirst();

		while(unitIterator.hasNext()) {
			final Unit unit = unitIterator.next();

			for (final ValueBox vbox : unit.getUseBoxes()) {
				if (vbox.getValue() instanceof StringConstant stringConst) {
					vbox.setValue(expandStringConstant(stringConst, initUnit, localGenerator));
					value.
				}
			}
			
		}
	}

}
