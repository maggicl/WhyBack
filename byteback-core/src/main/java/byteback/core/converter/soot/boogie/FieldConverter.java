package byteback.core.converter.soot.boogie;

import byteback.core.representation.soot.unit.SootFieldUnit;
import byteback.frontend.boogie.ast.ConstantDeclaration;
import byteback.frontend.boogie.ast.Declarator;
import byteback.frontend.boogie.ast.SetBinding;
import byteback.frontend.boogie.ast.TypeAccess;

public class FieldConverter {

	private static final FieldConverter instance = new FieldConverter();

	public static FieldConverter instance() {
		return instance;
	}

	public ConstantDeclaration convert(final SootFieldUnit fieldUnit) {
		final ConstantDeclaration boogieConstantDeclaration = new ConstantDeclaration();
		final SetBinding boogieBinding = new SetBinding();
		final TypeAccess boogieBaseTypeAccess = new TypeAccessExtractor().visit(fieldUnit.getType());
		final TypeAccess boogieFieldTypeAccess = Prelude.getFieldTypeAccess(boogieBaseTypeAccess);
		boogieConstantDeclaration.setBinding(boogieBinding);
		boogieBinding.setTypeAccess(boogieFieldTypeAccess);
		boogieBinding.addDeclarator(new Declarator(NameConverter.fieldName(fieldUnit)));

		return boogieConstantDeclaration;
	}

}
