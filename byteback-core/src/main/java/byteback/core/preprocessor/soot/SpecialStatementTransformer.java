package byteback.core.preprocessor.soot;

import soot.Body;
import soot.Transformer;

public class SpecialStatementTransformer extends Transformer {

	final Body body;

	public SpecialStatementTransformer(final Body body) {
		this.body = body;
	}

	public Body transform() {

		return null;
	}

}
