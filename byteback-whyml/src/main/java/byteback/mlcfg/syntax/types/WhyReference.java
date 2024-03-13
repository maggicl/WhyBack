package byteback.mlcfg.syntax.types;

public class WhyReference implements WhyPtrType {
	private final String classWhyScope;

	public WhyReference(String classWhyScope) {
		this.classWhyScope = classWhyScope;
	}

	@Override
	public String getWhyAccessorScope() {
		return "L";
	}

	@Override
	public String getPreludeType() {
		return "Class %s.class".formatted(classWhyScope);
	}
}
