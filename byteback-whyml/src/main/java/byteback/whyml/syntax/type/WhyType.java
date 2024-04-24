package byteback.whyml.syntax.type;

import java.util.Comparator;

public interface WhyType {
	Comparator<WhyType> ORDER = Comparator.comparing(WhyType::getDescriptor);

	/**
	 * Checks if a variable of type lValue can hold a value of type rValue. Does not check class hierarchy, only primitive
	 * and array compatibility
	 *
	 * @param lValue type of the l-value
	 * @param rValue type of the r-value
	 * @return true if compatible, false if not
	 */
	static boolean jvmCompatible(WhyType lValue, WhyJVMType rValue) {
		if (lValue instanceof WhyJVMType) {
			return lValue == rValue;
		} else if (lValue instanceof WhyReference) {
			return rValue != WhyJVMType.UNIT;
		}
		if (lValue instanceof WhyArrayType) {
			return rValue == WhyJVMType.PTR;
		}
		return false;
	}

	String getWhyType();

	WhyJVMType jvm();

	String getPreludeType();

	void accept(WhyTypeVisitor visitor);

	/**
	 * Returns the scope of where the get/put (load/store/(a)newarray for arrays) WhyML prelude function definitions
	 * for this type are located.
	 *
	 * @return A WhyML scope
	 */
	String getWhyAccessorScope();

	String getDescriptor();
}
