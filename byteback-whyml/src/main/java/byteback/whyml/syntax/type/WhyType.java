package byteback.whyml.syntax.type;

public interface WhyType {

	/**
	 * Checks if a variable of type lValue can hold a value of type rValue. Does not check class hierarchy, only primitive
	 * and array compatibility
	 *
	 * @param lValue type of the l-value
	 * @param rValue type of the r-value
	 * @return true if compatible, false if not
	 */
	static boolean compatible(WhyType lValue, WhyType rValue) {
		if (lValue instanceof WhyJVMType && rValue instanceof WhyJVMType) {
			return lValue == rValue;
		} else if (lValue instanceof WhyReference) {
			return true;
		}
		if (lValue instanceof WhyArrayType && rValue instanceof WhyArrayType) {
			return compatible(((WhyArrayType) lValue).baseType(), ((WhyArrayType) rValue).baseType());
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
}
