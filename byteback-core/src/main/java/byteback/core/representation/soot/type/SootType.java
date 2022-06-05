package byteback.core.representation.soot.type;

import byteback.core.representation.Visitable;
import soot.BooleanType;
import soot.ByteType;
import soot.DoubleType;
import soot.FloatType;
import soot.IntType;
import soot.LongType;
import soot.ShortType;

/**
 * Encapsulates a Soot type representation, visitable using a
 * {@link SootTypeVisitor}.
 *
 * This type will eventually also be used as the base type for all Soot types
 * wrappers. For now this wrapping is not considered necessary, as there are no
 * features that are planned to be added to the Soot type hierarchy.
 *
 * @author paganma
 */
public class SootType implements Visitable<SootTypeVisitor<?>> {

	private final soot.Type sootType;

	public static SootType booleanType() {
		return new SootType(BooleanType.v());
	}

	public static SootType byteType() {
		return new SootType(ByteType.v());
	}

	public static SootType shortType() {
		return new SootType(ShortType.v());
	}

	public static SootType intType() {
		return new SootType(IntType.v());
	}

	public static SootType longType() {
		return new SootType(LongType.v());
	}

	public static SootType floatType() {
		return new SootType(FloatType.v());
	}

	public static SootType doubleType() {
		return new SootType(DoubleType.v());
	}

	/**
	 * Constructs a {@link SootType} from a {@link soot.Type} instance.
	 *
	 * @param sootType
	 *            The given {@link soot.Type} instance.
	 */
	public SootType(final soot.Type sootType) {
		this.sootType = sootType;
	}

	public SootType getMachineType() {
		return new SootType(soot.Type.toMachineType(sootType));
	}

	/**
	 * Getter for the number of the type.
	 *
	 * This number is given dynamically by the underlying {@link soot.Scene}, and
	 * should not be assumed to be always the same across multiple contexts.
	 *
	 * @return The number associated with this type.
	 */
	public int getNumber() {
		return sootType.getNumber();
	}

	/**
	 * Applies the {@link SootTypeVisitor} to the wrapped {@link soot.Type}
	 * instance.
	 *
	 * @param visitor
	 *            The visitor to be applied.
	 */
	@Override
	public void apply(final SootTypeVisitor<?> visitor) {
		sootType.apply(visitor);
	}

	/**
	 * Checks the equality of two types based on their number.
	 *
	 * @param object
	 *            The given object instance to be checked against this instance.
	 * @return {@code true} if the given object is a {@link SootType} and has the
	 *         same number as this instance.
	 */
	@Override
	public boolean equals(Object object) {
		return object instanceof SootType && getNumber() == ((SootType) object).getNumber();
	}

	@Override
	public String toString() {
		return sootType.toString();
	}

}
