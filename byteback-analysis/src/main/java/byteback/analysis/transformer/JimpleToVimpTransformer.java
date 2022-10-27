package byteback.analysis.transformer;

import soot.Body;

public class JimpleToVimpTransformer extends ComposableTransformer {

	public JimpleToVimpTransformer() {
		super();
		initialize();
	}

	public final void initialize() {
		addTransformer(LogicUnitTransformer.v());
		addTransformer(LogicValueTransformer.v());
		addTransformer(QuantifierValueTransformer.v());
		addTransformer(AggregationTransformer.v());
	}

	@Override
	public void transformBody(final Body body) {
		super.transformBody(body);
		new FoldingTransformer().transformBody(body);
	}

}
