package byteback.core.converter.soottoboogie;

import byteback.frontend.boogie.ast.BoundedBinding;
import byteback.frontend.boogie.ast.Label;
import byteback.frontend.boogie.ast.TypeAccess;
import byteback.frontend.boogie.ast.ValueReference;
import byteback.frontend.boogie.builder.BoundedBindingBuilder;

/**
 * Conventions used to generate code that does not directly translates from
 * Jimple.
 *
 * @author paganma
 */
public class Convention {

	/**
	 * Builds a procedure's return binding.
	 *
	 * @param typeAccess
	 *            The type of the return binding.
	 * @return The {@code ~ret} {@link BoundedBinding}.
	 */
	public static BoundedBinding makeReturnBinding(final TypeAccess typeAccess) {
		return new BoundedBindingBuilder().addName("~ret").typeAccess(typeAccess).build();
	}

	/**
	 * Builds a reference to the procedure's return binding.
	 *
	 * @return The {@code ~ret} {@link ValueReference}.
	 */
	public static ValueReference makeReturnReference() {
		return ValueReference.of("~ret");
	}

	/**
	 * Creates a new numbered label statement.
	 *
	 * @param index
	 *            The index of the label.
	 * @return The new {@link Label} statement.
	 */
	public static Label makeLabelStatement(final int index) {
		return new Label("label" + index);
	}

	/**
	 * Creates a new temporary value reference from a unique index.
	 *
	 * @param index
	 *            The index of the temporary variable.
	 * @return The new {@link ValueReference} to the temporary variable.
	 */
	public static ValueReference makeValueReference(final int index) {
		return ValueReference.of("~sym" + index);
	}

	public static String makeParameterName(final String name) {
		return "!" + name;
	}

}
