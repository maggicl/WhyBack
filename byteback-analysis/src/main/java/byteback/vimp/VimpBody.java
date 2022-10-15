package byteback.vimp;

import byteback.core.converter.soottoboogie.method.procedure.DefinitionsCollector;
import soot.Body;
import soot.SootMethod;
import soot.jimple.JimpleBody;
import soot.jimple.StmtBody;

public class VimpBody extends StmtBody {

	public final DefinitionsCollector defCollector;

	VimpBody(final SootMethod method) {
		this(method.getActiveBody());
	}

	@Override
	public Body clone() {
		Body b = Vimp.v().newBody(getMethodUnsafe());
		b.importBodyContentsFrom(this);

		return b;
	}

	VimpBody(final Body body) {
		super(body.getMethod());

		if (body instanceof JimpleBody jimpleBody) {
			defCollector = new DefinitionsCollector();
			defCollector.collect(method.getActiveBody());
			construct(jimpleBody);
		} else {
			throw new IllegalArgumentException("Can construct VimpBody only from Jimple");
		}
	}

	private final void construct(final JimpleBody body) {
	}

}
