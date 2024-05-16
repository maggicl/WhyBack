package byteback.whyml.vimp.expr;

import soot.Unit;
import soot.Value;

public class WhyTranslationException extends RuntimeException {
	private final Value value;
	private final Unit unit;

	public WhyTranslationException(Value value, String message) {
		super(message);
		this.unit = null;
		this.value = value;
	}

	public WhyTranslationException(Unit unit, String message) {
		super(message);
		this.unit = unit;
		this.value = null;
	}

	public Unit unit() {
		return unit;
	}

	public Value value() {
		return value;
	}
}
