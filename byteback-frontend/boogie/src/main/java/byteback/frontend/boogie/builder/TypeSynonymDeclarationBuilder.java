package byteback.frontend.boogie.builder;

import byteback.frontend.boogie.ast.TypeAccess;
import byteback.frontend.boogie.ast.TypeSynonymDeclaration;

public class TypeSynonymDeclarationBuilder extends TypeDeclarationBuilder<TypeSynonymDeclaration> {

    public TypeSynonymDeclarationBuilder() {
        super(new TypeSynonymDeclaration());
    }

    public TypeSynonymDeclarationBuilder aliased(final TypeAccess typeAccess) {
        typeDeclaration.setAliased(typeAccess);

        return this;
    }

}
