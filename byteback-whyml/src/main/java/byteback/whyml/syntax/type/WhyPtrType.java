package byteback.whyml.syntax.type;

public interface WhyPtrType extends WhyType {
	@Override
	default String getWhyType() {
		return WhyJVMType.PTR.getWhyType();
	}

	@Override
	default WhyJVMType jvm() { return WhyJVMType.PTR; }
}
