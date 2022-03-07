package byteback.frontend.boogie.builder;

import byteback.frontend.boogie.ast.TypeAccess;

public class TypeAccessBuilder<T extends TypeAccess> {

    protected T typeAccess;

    public TypeAccessBuilder(T typeAccess) {
        this.typeAccess = typeAccess;
    }

    public TypeAccess build() {
        return typeAccess;
    }

}
