package byteback.core.converter.soot.boogie;

import byteback.core.representation.soot.type.SootType;
import byteback.core.representation.soot.unit.SootFieldUnit;
import byteback.core.representation.soot.unit.SootMethodUnit;
import java.util.Iterator;

public class NameConverter {

	static String methodName(SootMethodUnit methodUnit) {
		final StringBuilder builder = new StringBuilder();
		final Iterator<SootType> typeIterator = methodUnit.getParameterTypes().iterator();
		builder.append(methodUnit.getClassUnit().getName());
		builder.append(".");
		builder.append(methodUnit.getName());
		builder.append("#");

		while (typeIterator.hasNext()) {
			builder.append(typeIterator.next());
			builder.append("#");
		}

		if (methodUnit.getParameterTypes().size() == 0) {
			builder.append("#");
		}

		return builder.toString();
	}

	static String fieldName(final SootFieldUnit fieldUnit) {
		final String fieldName = fieldUnit.getName();
		final String className = fieldUnit.getClassUnit().getName();

		return className + "." + fieldName;
	}

}
