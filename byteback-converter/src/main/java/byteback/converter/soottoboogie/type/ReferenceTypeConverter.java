package byteback.converter.soottoboogie.type;

import byteback.converter.soottoboogie.Prelude;
import byteback.frontend.boogie.ast.ConstantDeclaration;
import byteback.frontend.boogie.ast.TypeAccess;
import byteback.frontend.boogie.builder.SetBindingBuilder;
import soot.SootClass;

public class ReferenceTypeConverter {

	private static final ReferenceTypeConverter instance = new ReferenceTypeConverter();

	public static String typeName(final SootClass clazz) {
		return "$" + clazz.getName();
	}

	public static ReferenceTypeConverter instance() {
		return instance;
	}

	public ConstantDeclaration convert(final SootClass clazz) {
		final var constantDeclaration = new ConstantDeclaration();
		final var bindingBuilder = new SetBindingBuilder();
		final TypeAccess typeAccess = Prelude.v().getTypeType().makeTypeAccess();
		bindingBuilder.typeAccess(typeAccess);
		bindingBuilder.name(typeName(clazz));
		constantDeclaration.setBinding(bindingBuilder.build());
		constantDeclaration.setUnique(true);

		return constantDeclaration;
	}

}
