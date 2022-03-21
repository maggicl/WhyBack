package byteback.core.converter.soot.boogie;

import byteback.core.representation.soot.unit.SootFieldUnit;
import byteback.frontend.boogie.ast.ConstantDeclaration;
import byteback.frontend.boogie.ast.Declarator;
import byteback.frontend.boogie.ast.SetBinding;
import byteback.frontend.boogie.ast.TypeAccess;

public class BoogieFieldExtractor {

    final SootFieldUnit fieldUnit;

    public BoogieFieldExtractor(final SootFieldUnit fieldUnit) {
        this.fieldUnit = fieldUnit;
    }

    public ConstantDeclaration convert() {
        final ConstantDeclaration boogieConstantDeclaration = new ConstantDeclaration();
        final SetBinding boogieBinding = new SetBinding();
        final TypeAccess boogieBaseTypeAccess = new BoogieTypeAccessExtractor().visit(fieldUnit.getType());
        final TypeAccess boogieFieldTypeAccess = BoogiePrelude.getFieldTypeAccess(boogieBaseTypeAccess);
        boogieConstantDeclaration.setBinding(boogieBinding);
        boogieBinding.setTypeAccess(boogieFieldTypeAccess);
        boogieBinding.addDeclarator(new Declarator(BoogieNameConverter.fieldName(fieldUnit)));

        return boogieConstantDeclaration;
    }

}
