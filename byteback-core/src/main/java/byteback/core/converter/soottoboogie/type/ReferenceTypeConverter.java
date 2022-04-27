package byteback.core.converter.soottoboogie.type;

import byteback.core.converter.soottoboogie.Prelude;
import byteback.core.representation.soot.unit.SootClass;
import byteback.frontend.boogie.ast.ConstantDeclaration;
import byteback.frontend.boogie.ast.TypeAccess;
import byteback.frontend.boogie.builder.SetBindingBuilder;

public class ReferenceTypeConverter {

	private static final ReferenceTypeConverter instance = new ReferenceTypeConverter();

	public static ReferenceTypeConverter instance() {
		return instance;
	}

	public ConstantDeclaration convert(final SootClass clazz) {
		final var constantDeclaration = new ConstantDeclaration();
		final var bindingBuilder = new SetBindingBuilder();
		final TypeAccess typeAccess = Prelude.instance().getTypeType().makeTypeAccess();
		bindingBuilder.typeAccess(typeAccess);
		bindingBuilder.name(clazz.getName());
		constantDeclaration.setBinding(bindingBuilder.build());
		constantDeclaration.setUnique(true);

		return constantDeclaration;
	}

}
