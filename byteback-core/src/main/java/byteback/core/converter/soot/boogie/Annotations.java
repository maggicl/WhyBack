package byteback.core.converter.soot.boogie;

import byteback.core.context.soot.ClassLoadException;
import byteback.core.context.soot.SootContext;
import byteback.core.representation.soot.unit.SootClass;
import byteback.core.representation.soot.unit.SootMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Annotations {

	private static final Logger log = LoggerFactory.getLogger(Annotations.class);

	public static final String CONTRACT_CLASS_NAME = "byteback.annotations.Contract";

	public static final String PRELUDE_ANNOTATION = "Lbyteback/annotations/Contract$Prelude;";

	public static final String PURE_ANNOTATION = "Lbyteback/annotations/Contract$Pure;";

	public static final String REQUIRE_ANNOTATION = "Lbyteback/annotations/Contract$Require;";

	public static final String ENSURE_ANNOTATION = "Lbyteback/annotations/Contract$Ensure;";

	public static final SootClass CONTRACT_CLASS = loadClass(CONTRACT_CLASS_NAME);

	public static final SootMethod ASSERT_METHOD = CONTRACT_CLASS.getSootMethod("assertion").get();

	public static final SootMethod ASSUME_METHOD = CONTRACT_CLASS.getSootMethod("assumption").get();

	private static SootClass loadClass(final String name) {
		try {
			return SootContext.instance().loadClass(name);
		} catch (final ClassLoadException exception) {
			log.error("Could not load annotation class: {}", name, exception);
			throw new RuntimeException(exception);
		}
	}

}
