package byteback.frontend.boogie.builder;

import byteback.frontend.boogie.ast.*;

public abstract class TypeConstructorDeclarationBuilder extends TypeDeclarationBuilder<TypeConstructorDeclaration> {

    private TypeConstructorDeclaration typeDeclaration;

    public TypeConstructorDeclarationBuilder() {
        super(new TypeConstructorDeclaration());
    }

    public TypeConstructorDeclarationBuilder finite() {
        typeDeclaration.setFinite(true);

        return this;
    }

}
