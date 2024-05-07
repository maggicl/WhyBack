package byteback.whyml.syntax.expr.field;

public enum Operation {
	GET("get"),
	IS("is");

	private final String whyKeyword;

	Operation(String whyKeyword) {
		this.whyKeyword = whyKeyword;
	}

	public String whyKeyword() {
		return whyKeyword;
	}
}