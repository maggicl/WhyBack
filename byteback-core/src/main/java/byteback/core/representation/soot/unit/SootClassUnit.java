package byteback.core.representation.soot.unit;

import byteback.core.representation.soot.type.SootType;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Stream;
import soot.SootClass;

/**
 * Wraps a {@link SootClass} intermediate representation.
 */
public class SootClassUnit {

	private final SootClass sootClass;

	/**
	 * Constructor the Soot class representation wrapper.
	 *
	 * @param sootClass
	 *            The wrapped {@link SootClass} class.
	 */
	public SootClassUnit(final SootClass sootClass) {
		this.sootClass = sootClass;
	}

	/**
	 * Verifies if the class is part of the basic classes set.
	 *
	 * The original Soot implementation performs simple checks on the prefix of the
	 * package. Oddly enough the list did not include the "jdk." prefix.
	 *
	 * @return {@code true} if the instance refers to a basic class.
	 */
	public boolean isBasicClass() {
		return sootClass.isJavaLibraryClass() || getName().startsWith("jdk");
	}

	/**
	 * Verifies if the class is a phantom class. A phantom class is a class that
	 * does not exist in the classpath.
	 *
	 * @return {@code true} if the instance refers to a phantom class.
	 */
	public boolean isPhantomClass() {
		return sootClass.isPhantomClass();
	}

	/**
	 * Getter for the qualified name of the class.
	 *
	 * @return The qualified name of the class.
	 */
	public String getName() {
		return sootClass.getName();
	}

	public SootType getType() {
		return new SootType(sootClass.getType());
	}

	/**
	 * Yields the stream of Soot methods present in the class.
	 *
	 * @return The stream of method representations.
	 */
	public Stream<SootMethodUnit> methods() {
		assert !isPhantomClass();

		return sootClass.getMethods().stream().map(SootMethodUnit::new);
	}

	/**
	 * Yields the stream of Soot fields present in the class.
	 *
	 * @return The stream of field representations.
	 */
	public Stream<SootFieldUnit> fields() {
		assert !isPhantomClass();

		return sootClass.getFields().stream().map(SootFieldUnit::new);
	}

	public Optional<SootMethodUnit> getMethodUnit(final String name, final Collection<SootType> parameterTypes,
			final SootType returnType) {

		final String signature = SootMethodUnit.formatSignature(name, parameterTypes, returnType);

		return Optional.ofNullable(new SootMethodUnit(sootClass.getMethod(signature)));
	}

}
