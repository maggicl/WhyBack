package byteback.frontend.boogie.builder;

import java.util.Optional;

import byteback.frontend.boogie.ast.*;

public class OptionalBindingBuilder extends BindingBuilder {

    protected Optional<Declarator> declaratorParameter;

    public OptionalBindingBuilder() {
        this.declaratorParameter = Optional.empty();
    }

    public OptionalBindingBuilder name(final String name) {
        declaratorParameter = Optional.of(new Declarator(name));

        return this;
    }

    public OptionalBindingBuilder typeAccess(final TypeAccess typeAccess) {
        super.typeAccess(typeAccess);

        return this;
    }

    public OptionalBinding build() {
        final TypeAccess typeAccess = typeAccessParameter
                .orElseThrow(() -> new IllegalArgumentException("Optional binding must include a type access"));
        final Opt<Declarator> declarator = declaratorParameter.map(Opt::new).orElse(new Opt<>());

        return new OptionalBinding(typeAccess, declarator);
    }

}
