package byteback.analysis.util;

import java.util.ArrayList;
import java.util.List;
import soot.Local;
import soot.SootClass;
import soot.SootMethod;
import soot.jimple.internal.JimpleLocal;

public class SootMethods {

	public static List<Local> makeFakeParameterLocals(final SootMethod method) {
		final List<Local> parameterLocals = new ArrayList<>();

		if (!method.isStatic()) {
			parameterLocals.add(new JimpleLocal("this", method.getDeclaringClass().getType()));
		}

		for (int i = 0; i < method.getParameterCount(); ++i) {
			final String name = "p" + i;
			parameterLocals.add(new JimpleLocal(name, method.getParameterType(i)));
		}

		return parameterLocals;
	}

	public static boolean hasBody(final SootMethod method) {
		try {
			return !method.isAbstract()
				&& method.getDeclaringClass().resolvingLevel() >= SootClass.BODIES
				&& method.retrieveActiveBody() != null;
		} catch (final RuntimeException exception) {
			return false;
		}
	}

}
