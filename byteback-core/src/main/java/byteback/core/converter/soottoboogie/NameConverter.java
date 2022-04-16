package byteback.core.converter.soottoboogie;

import byteback.core.representation.soot.type.SootType;
import byteback.core.representation.soot.unit.SootField;
import byteback.core.representation.soot.unit.SootMethod;
import java.util.Iterator;

public class NameConverter {

	public static String methodName(final SootMethod method) {
		final var builder = new StringBuilder();
		final Iterator<SootType> typeIterator = method.getParameterTypes().iterator();
		final String methodName = method.getName();
		builder.append(method.getSootClass().getName());
		builder.append(".");
		builder.append(methodName.replace("<", "$").replace(">", "$"));
		builder.append("#");

		while (typeIterator.hasNext()) {
			builder.append(typeIterator.next().toString().replace("[", "").replace("]", "$"));
			builder.append("#");
		}

		if (method.getParameterTypes().size() == 0) {
			builder.append("#");
		}

		return builder.toString();
	}

	public static String fieldName(final SootField field) {
		final String fieldName = field.getName();
		final String className = field.getSootClass().getName();

		return className + "." + fieldName;
	}

}
