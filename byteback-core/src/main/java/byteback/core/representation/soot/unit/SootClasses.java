package byteback.core.representation.soot.unit;

import soot.SootClass;

public class SootClasses {

	public static boolean isBasicClass(final SootClass clazz) {
		final String name = clazz.getName();

		return clazz.isJavaLibraryClass() || name.startsWith("jdk") || name.startsWith("scala");
	}

}
