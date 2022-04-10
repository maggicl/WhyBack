package byteback.core.converter.soottoboogie;

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

	public static final SootClass CONTRACT_CLASS = loadContractClass();

	public static final SootMethod ASSERT_METHOD = CONTRACT_CLASS.getSootMethod("assertion")
			.orElseThrow(() -> new RuntimeException("Could not load `assertion` method from Contract class"));

	public static final SootMethod ASSUME_METHOD = CONTRACT_CLASS.getSootMethod("assumption")
			.orElseThrow(() -> new RuntimeException("Could not load `assumption` method from Contract class"));

	public static final SootMethod INVARIANT_METHOD = CONTRACT_CLASS.getSootMethod("invariant")
			.orElseThrow(() -> new RuntimeException("Could not load `invariant` method from Contract class"));

	private static SootClass loadContractClass() {
		try {
			return SootContext.instance().loadClass(CONTRACT_CLASS_NAME);
		} catch (final ClassLoadException exception) {
			log.error("Could not load Contract class", exception);
			throw new RuntimeException(exception);
		}
	}

}
