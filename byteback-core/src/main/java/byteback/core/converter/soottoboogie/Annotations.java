package byteback.core.converter.soottoboogie;

import byteback.core.context.soot.ClassLoadException;
import byteback.core.context.soot.SootContext;
import byteback.core.representation.soot.unit.SootClass;
import byteback.core.representation.soot.unit.SootMethod;
import byteback.core.util.Lazy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Annotations {

	private static final Logger log = LoggerFactory.getLogger(Annotations.class);

	public static final String CONTRACT_CLASS_NAME = "byteback.annotations.Contract";

	public static final String QUANTIFIER_CLASS_NAME = "byteback.annotations.Quantifier";

	public static final String SPECIAL_CLASS_NAME = "byteback.annotations.Special";

	public static final String PRELUDE_ANNOTATION = "Lbyteback/annotations/Contract$Prelude;";

	public static final String PURE_ANNOTATION = "Lbyteback/annotations/Contract$Pure;";

	public static final String REQUIRE_ANNOTATION = "Lbyteback/annotations/Contract$Require;";

	public static final String ENSURE_ANNOTATION = "Lbyteback/annotations/Contract$Ensure;";

	public static final String OLD_NAME = "old";

	public static final String CONDITIONAL_NAME = "ifThenElse";

	public static final String UNIVERSAL_QUANTIFIER_NAME = "forall";

	public static final String EXISTENTIAL_QUANTIFIER_NAME = "exists";

	public static final Lazy<SootClass> CONTRACT_CLASS = Lazy.from(() -> loadClass(CONTRACT_CLASS_NAME));

	public static final Lazy<SootClass> QUANTIFIER_CLASS = Lazy.from(() -> loadClass(QUANTIFIER_CLASS_NAME));

	public static final Lazy<SootClass> SPECIAL_CLASS = Lazy.from(() -> loadClass(SPECIAL_CLASS_NAME));

	public static final Lazy<SootMethod> ASSERT_METHOD = Lazy
    .from(() -> CONTRACT_CLASS.get().getSootMethod("assertion")
			.orElseThrow(() -> new RuntimeException("Could not load `assertion` method from Contract class")));

	public static final Lazy<SootMethod> ASSUME_METHOD = Lazy
			.from(() -> CONTRACT_CLASS.get().getSootMethod("assumption")
					.orElseThrow(() -> new RuntimeException("Could not load `assumption` method from Contract class")));

	public static final Lazy<SootMethod> INVARIANT_METHOD = Lazy
			.from(() -> CONTRACT_CLASS.get().getSootMethod("invariant")
					.orElseThrow(() -> new RuntimeException("Could not load `invariant` method from Contract class")));

	public static void reset() {
		CONTRACT_CLASS.invalidate();
		QUANTIFIER_CLASS.invalidate();
    SPECIAL_CLASS.invalidate();
		ASSERT_METHOD.invalidate();
		ASSUME_METHOD.invalidate();
		INVARIANT_METHOD.invalidate();
	}

	private static SootClass loadClass(final String className) {
		try {
			return SootContext.instance().loadClassAndSupport(className);
		} catch (final ClassLoadException exception) {
			log.error("Could not load base class {}", className, exception);
			throw new RuntimeException(exception);
		}
	}

}
