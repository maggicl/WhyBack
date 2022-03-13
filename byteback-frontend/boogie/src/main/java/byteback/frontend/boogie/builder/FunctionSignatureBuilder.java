package byteback.frontend.boogie.builder;

import java.util.Optional;

import byteback.frontend.boogie.ast.FunctionSignature;
import byteback.frontend.boogie.ast.List;
import byteback.frontend.boogie.ast.OptionalBinding;
import byteback.frontend.boogie.ast.TypeParameter;

public class FunctionSignatureBuilder extends DeclarationBuilder {

    private List<TypeParameter> typeParameters;

    private List<OptionalBinding> inputBindings;

    private Optional<OptionalBinding> outputBindingParameter;

    public FunctionSignatureBuilder() {
        this.typeParameters = new List<>();
        this.inputBindings = new List<>();
        this.outputBindingParameter = Optional.empty();
    }

    public FunctionSignatureBuilder addTypeParameter(final TypeParameter typeParameter) {
        this.typeParameters.add(typeParameter);

        return this;
    }

    public FunctionSignatureBuilder addInputBinding(final OptionalBinding inputBinding) {
        this.inputBindings.add(inputBinding);

        return this;
    }

    public FunctionSignatureBuilder outputBinding(final OptionalBinding optionalBinding) {
        outputBindingParameter = Optional.of(optionalBinding);

        return this;
    }

    public FunctionSignature build() {
        final OptionalBinding outputBinding = outputBindingParameter.orElseThrow(() -> {
            throw new IllegalArgumentException("A function signature must define an output binding");
        });

        return new FunctionSignature(typeParameters, inputBindings, outputBinding);
    }

}
