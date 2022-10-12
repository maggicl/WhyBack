package byteback.core.converter.soottoboogie.method;

import java.util.Iterator;
import soot.SootMethod;
import soot.Type;

public abstract class MethodConverter {

	public static String methodName(final SootMethod method) {
		final var builder = new StringBuilder();
		final Iterator<Type> typeIterator = method.getParameterTypes().iterator();
		final String methodName = method.getName();
		builder.append(method.getDeclaringClass().getName());
		builder.append(".");
		builder.append(methodName.replace("<", "$").replace(">", "$"));
		builder.append("#");

		while (typeIterator.hasNext()) {
			builder.append(typeIterator.next().toString().replace("[", "").replace("]", "?"));
			builder.append("#");
		}

		if (method.getParameterTypes().size() == 0) {
			builder.append("#");
		}

		return builder.toString();
	}

}
