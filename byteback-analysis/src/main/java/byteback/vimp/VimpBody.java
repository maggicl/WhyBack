package byteback.vimp;

import java.util.Iterator;

import byteback.core.converter.soottoboogie.method.procedure.DefinitionsCollector;
import byteback.vimp.internal.AssertionStmt;
import soot.Body;
import soot.SootClass;
import soot.SootMethod;
import soot.Unit;
import soot.Value;
import soot.jimple.AbstractStmtSwitch;
import soot.jimple.InvokeExpr;
import soot.jimple.InvokeStmt;
import soot.jimple.JimpleBody;
import soot.jimple.StmtBody;

public class VimpBody extends StmtBody {

	VimpBody(final SootMethod method) {
		super(method);
	}

	@Override
	public Body clone() {
		Body b = Vimp.v().newBody(getMethodUnsafe());
		b.importBodyContentsFrom(this);

		return b;
	}

	VimpBody(final JimpleBody body) {
		super(body.getMethod());
		construct(body);
	}

	private final void construct(final JimpleBody body) {
		final Body newBody = (Body) body.clone();
		final Iterator<Unit> iterator = body.getUnits().snapshotIterator();
		final DefinitionsCollector defCollector = new DefinitionsCollector();
		defCollector.collect(body);

		while (iterator.hasNext()) {
			final Unit unit = iterator.next();

			unit.apply(new AbstractStmtSwitch<>() {

					@Override
					public void caseInvokeStmt(final InvokeStmt stmt) {
						final InvokeExpr expr = stmt.getInvokeExpr();
						final SootMethod method = expr.getMethod();
						final SootClass clazz = method.getDeclaringClass();

						if (Namespace.isContractClass(clazz)) {
							final String name = method.getName();

							if (name.equals(Namespace.ASSERTION_NAME)) {
								final AssertionStmt assertionStmt = Vimp.v().
							}
						}
					}
			});
		}
	}

}
