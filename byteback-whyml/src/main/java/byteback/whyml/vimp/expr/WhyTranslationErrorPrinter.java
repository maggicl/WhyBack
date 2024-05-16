package byteback.whyml.vimp.expr;

import soot.Body;
import soot.NormalUnitPrinter;
import soot.Unit;
import soot.ValueBox;

public class WhyTranslationErrorPrinter extends NormalUnitPrinter {
	private final WhyTranslationException e;
	private static final String START = "\u001B[31m";

	private static final String EXPR_START = "\u001B[35m";
	private static final String END = "\u001B[0m";

	private static final String EXPR_END = "\u001B[0m";


	public WhyTranslationErrorPrinter(Body body, WhyTranslationException e) {
		super(body);
		this.e = e;
	}

	@Override
	public void startUnit(Unit u) {
		if (u == e.unit()) {
			output.append(START);
		}

		super.startUnit(u);
	}

	@Override
	public void endUnit(Unit u) {
		if (u == e.unit()) {
			output.append(END);
		}

		super.endUnit(u);
	}

	@Override
	public void startValueBox(ValueBox vb) {
		if (vb.getValue() == e.value()) {
			output.append(EXPR_START);
		}

		super.startValueBox(vb);
	}

	@Override
	public void endValueBox(ValueBox vb) {
		if (vb.getValue() == e.value()) {
			output.append(EXPR_END);
		}

		super.endValueBox(vb);
	}
}
