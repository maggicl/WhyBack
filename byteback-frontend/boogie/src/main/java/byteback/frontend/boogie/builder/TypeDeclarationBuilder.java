package byteback.frontend.boogie.builder;

import byteback.frontend.boogie.ast.*;

public abstract class TypeDeclarationBuilder<T extends TypeDeclaration> {

    protected T typeDeclaration;

    protected TypeDeclarationBuilder(final T typeDeclaration) {
        this.typeDeclaration = typeDeclaration;
    }

    public TypeDeclarationBuilder<T> name(final String name) {
        typeDeclaration.setDeclarator(new Declarator(name));

        return this;
    }

    public TypeDeclarationBuilder<T> parameter(final String parameter) {
        typeDeclaration.addTypeParameter(new TypeParameter(new Declarator(parameter)));

        return this;
    }

    public TypeDeclaration build() {
        return typeDeclaration;
    }
}
