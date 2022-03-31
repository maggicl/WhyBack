package byteback.core.converter.soot.boogie;

import byteback.core.representation.soot.unit.SootField;
import byteback.frontend.boogie.ast.ConstantDeclaration;
import byteback.frontend.boogie.ast.Declarator;
import byteback.frontend.boogie.ast.SetBinding;
import byteback.frontend.boogie.ast.TypeAccess;

public class FieldConverter {

	private static final FieldConverter instance = new FieldConverter();

	public static FieldConverter instance() {
		return instance;
	}

	public ConstantDeclaration convert(final SootField fieldUnit) {
		final ConstantDeclaration constantDeclaration = new ConstantDeclaration();
		final SetBinding binding = new SetBinding();
		final TypeAccess baseTypeAccess = new TypeAccessExtractor().visit(fieldUnit.getType());
		final TypeAccess fieldTypeAccess = Prelude.getFieldTypeAccess(baseTypeAccess);
		constantDeclaration.setBinding(binding);
		binding.setTypeAccess(fieldTypeAccess);
		binding.addDeclarator(new Declarator(NameConverter.fieldName(fieldUnit)));

		return constantDeclaration;
	}

}
