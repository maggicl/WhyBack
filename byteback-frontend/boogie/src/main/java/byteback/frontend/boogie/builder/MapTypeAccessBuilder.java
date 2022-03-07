package byteback.frontend.boogie.builder;

import byteback.frontend.boogie.ast.*;

public class MapTypeAccessBuilder extends TypeAccessBuilder<MapTypeAccess> {

    public MapTypeAccessBuilder() {
        super(new MapTypeAccess());
    }

    public MapTypeAccessBuilder typeParameter(final String parameter) {
        typeAccess.addTypeParameter(new TypeParameter(new Declarator(parameter)));

        return this;
    }

}
