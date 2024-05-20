package byteback.whyml.syntax.type;

import byteback.whyml.printer.SExpr;
import java.util.Comparator;

public interface WhyType {
	Comparator<WhyType> ORDER = Comparator.comparing(WhyType::getDescriptor);

	String getWhyType();

	WhyJVMType jvm();

	SExpr getPreludeType();

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
