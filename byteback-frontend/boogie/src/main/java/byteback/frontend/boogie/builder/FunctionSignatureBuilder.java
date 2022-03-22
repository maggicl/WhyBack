package byteback.frontend.boogie.builder;

import byteback.frontend.boogie.ast.*;
import java.util.Optional;

public class FunctionSignatureBuilder extends SignatureBuilder {

	private List<OptionalBinding> inputBindings;

	private Optional<OptionalBinding> outputBindingParameter;

	public FunctionSignatureBuilder() {
		this.inputBindings = new List<>();
		this.outputBindingParameter = Optional.empty();
	}

	public FunctionSignatureBuilder addInputBinding(final OptionalBinding inputBinding) {
		this.inputBindings.add(inputBinding);

		return this;
	}

	public FunctionSignatureBuilder outputBinding(final OptionalBinding outputBinding) {
		outputBindingParameter = Optional.of(outputBinding);

		return this;
	}

	public FunctionSignature build() {
		final OptionalBinding outputBinding = outputBindingParameter.orElseThrow(() -> {
			throw new IllegalArgumentException("A function signature must define an output binding");
		});

		return new FunctionSignature(typeParameters, inputBindings, outputBinding);
	}

}
