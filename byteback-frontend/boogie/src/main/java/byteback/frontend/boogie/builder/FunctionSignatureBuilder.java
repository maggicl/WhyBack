import java.util.Optional;

import byteback.frontend.boogie.ast.FunctionSignature;
import byteback.frontend.boogie.ast.List;
import byteback.frontend.boogie.ast.OptionalBinding;

public class FunctionSignatureBuilder extends DeclarationBuilder {

    private List<OptionalBinding> inputBindings;

    private Optional<OptionalBinding> outputBindingParameter;

    public FunctionDeclarationBuilder() {
        this.inputBindings = new OptionalBinding();
        this.outputBindingParameter = new OptionalBinding();
    }

    public FunctionSignature build() {
        return null;
    }

}
