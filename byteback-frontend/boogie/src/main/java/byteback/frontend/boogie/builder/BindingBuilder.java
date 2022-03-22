package byteback.frontend.boogie.builder;

import java.util.Optional;

import byteback.frontend.boogie.ast.*;

public abstract class BindingBuilder {

    protected Optional<TypeAccess> typeAccessParameter;

    public BindingBuilder() {
        this.typeAccessParameter = Optional.empty();
    }

    public BindingBuilder typeAccess(final TypeAccess typeAccess) {
        this.typeAccessParameter = Optional.of(typeAccess);

        return this;
    }
    
}
