package byteback.analysis.transformer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import soot.Body;
import soot.BodyTransformer;
import soot.UnitBox;
import soot.ValueBox;

public class ComposableTransformer extends BodyTransformer implements UnitValueTransformer {

	final List<ValueTransformer> valueTransformers;

	final List<UnitTransformer> unitTransformers;

	public ComposableTransformer() {
		this(new ArrayList<>(), new ArrayList<>());
	}

	public ComposableTransformer(final List<ValueTransformer> valueTransformers,
			final List<UnitTransformer> unitTransformers) {
		this.valueTransformers = valueTransformers;
		this.unitTransformers = unitTransformers;
	}

	@Override
	protected void internalTransform(final Body body, String phaseName, Map<String, String> options) {
		transformBody(body);
	}

	public void addTransformer(final ValueTransformer transformer) {
		valueTransformers.add(transformer);
	}

	public void addTransformer(final UnitTransformer transformer) {
		unitTransformers.add(transformer);
	}

	public void addTransformer(final UnitValueTransformer transformer) {
		unitTransformers.add((UnitTransformer) transformer);
		valueTransformers.add((ValueTransformer) transformer);
	}

	public void transformUnit(final UnitBox unitBox) {
		for (final UnitTransformer transformer : unitTransformers) {
			transformer.transformUnit(unitBox);
		}
	}

	public void transformValue(final ValueBox valueBox) {
		for (final ValueTransformer transformer : valueTransformers) {
			transformer.transformValue(valueBox);
		}
	}

}
