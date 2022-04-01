package byteback.core.converter.soot.boogie;

import byteback.core.representation.soot.type.SootType;
import byteback.core.representation.soot.unit.SootField;
import byteback.core.representation.soot.unit.SootMethod;
import java.util.Iterator;

public class NameConverter {

	static String methodName(SootMethod method) {
		final StringBuilder builder = new StringBuilder();
		final Iterator<SootType> typeIterator = method.getParameterTypes().iterator();
		builder.append(method.getSootClass().getName());
		builder.append(".");
		builder.append(method.getName());
		builder.append("#");

		while (typeIterator.hasNext()) {
			builder.append(typeIterator.next());
			builder.append("#");
		}

		if (method.getParameterTypes().size() == 0) {
			builder.append("#");
		}

		return builder.toString();
	}

	static String fieldName(final SootField field) {
		final String fieldName = field.getName();
		final String className = field.getSootClass().getName();

		return className + "." + fieldName;
	}

}
