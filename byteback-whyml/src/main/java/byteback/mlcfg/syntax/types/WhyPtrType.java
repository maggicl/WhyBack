package byteback.mlcfg.syntax.types;

public interface WhyPtrType extends WhyType {
	@Override
	default String getWhyType() {
		return WhyJVMType.PTR.getWhyType();
	}
}
