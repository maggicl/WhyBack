package byteback.core.converter.soottoboogie.field;

import byteback.core.converter.soottoboogie.ConversionException;
import byteback.core.converter.soottoboogie.Prelude;
import byteback.core.converter.soottoboogie.type.TypeAccessExtractor;
import byteback.frontend.boogie.ast.ConstantDeclaration;
import byteback.frontend.boogie.ast.TypeAccess;
import byteback.frontend.boogie.builder.SetBindingBuilder;
import soot.SootField;

public class FieldConverter {

	private static final FieldConverter instance = new FieldConverter();

	public static FieldConverter instance() {
		return instance;
	}

	public static String fieldName(final SootField field) {
		final String fieldName = field.getName();
		final String className = field.getDeclaringClass().getName();

		return "$" + className + "." + fieldName;
	}

	public ConstantDeclaration convert(final SootField field) {
		final var constantDeclaration = new ConstantDeclaration();
		final var bindingBuilder = new SetBindingBuilder();
		final TypeAccess baseTypeAccess = new TypeAccessExtractor().visit(field.getType());
		final TypeAccess fieldTypeAccess = Prelude.v().makeFieldTypeAccess(baseTypeAccess);

		try {
			bindingBuilder.typeAccess(fieldTypeAccess);
			bindingBuilder.name(fieldName(field));
			constantDeclaration.setBinding(bindingBuilder.build());
			constantDeclaration.setUnique(true);
		} catch (ConversionException exception) {
			throw new FieldConversionException(field, exception);
		}

		return constantDeclaration;
	}

}
