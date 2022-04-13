package byteback.core.converter.soottoboogie.field;

import byteback.core.converter.soottoboogie.NameConverter;
import byteback.core.converter.soottoboogie.Prelude;
import byteback.core.converter.soottoboogie.type.TypeAccessExtractor;
import byteback.core.representation.soot.unit.SootField;
import byteback.frontend.boogie.ast.ConstantDeclaration;
import byteback.frontend.boogie.ast.TypeAccess;
import byteback.frontend.boogie.builder.SetBindingBuilder;

public class FieldConverter {

	private static final FieldConverter instance = new FieldConverter();

	public static FieldConverter instance() {
		return instance;
	}

	public ConstantDeclaration convert(final SootField field) {
		final ConstantDeclaration constantDeclaration = new ConstantDeclaration();
		final var bindingBuilder = new SetBindingBuilder();
		final TypeAccess baseTypeAccess = new TypeAccessExtractor().visit(field.getType());
		final TypeAccess fieldTypeAccess = Prelude.getFieldTypeAccess(baseTypeAccess);
		bindingBuilder.typeAccess(fieldTypeAccess);
		bindingBuilder.name(NameConverter.fieldName(field));
		constantDeclaration.setBinding(bindingBuilder.build());

		return constantDeclaration;
	}

}
