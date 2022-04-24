package byteback.core.converter.soottoboogie;

import byteback.core.representation.soot.unit.SootClass;

public class AnnotationContext {

  public static final String ANNOTATION_PACKAGE = "byteback.annotations";

	public static final String CONTRACT_CLASS_NAME = "byteback.annotations.Contract";

	public static final String QUANTIFIER_CLASS_NAME = "byteback.annotations.Quantifier";

	public static final String SPECIAL_CLASS_NAME = "byteback.annotations.Special";

	public static final String BINDING_CLASS_NAME = "byteback.annotations.Binding";

	public static final String PRELUDE_ANNOTATION = "Lbyteback/annotations/Contract$Prelude;";

	public static final String PURE_ANNOTATION = "Lbyteback/annotations/Contract$Pure;";

	public static final String CONDITION_ANNOTATION = "Lbyteback/annotations/Contract$Condition;";

	public static final String REQUIRE_ANNOTATION = "Lbyteback/annotations/Contract$Require;";

	public static final String ENSURE_ANNOTATION = "Lbyteback/annotations/Contract$Ensure;";

	public static final String OLD_NAME = "old";

	public static final String INVARIANT_NAME = "invariant";

	public static final String ASSUMPTION_NAME = "assumption";

	public static final String ASSERTION_NAME = "assertion";

	public static final String CONDITIONAL_NAME = "ifThenElse";

	public static final String UNIVERSAL_QUANTIFIER_NAME = "forall";

	public static final String EXISTENTIAL_QUANTIFIER_NAME = "exists";

  public static boolean isContractClass(final SootClass clazz) {
    return clazz.getName().equals(CONTRACT_CLASS_NAME);
  }

  public static boolean isQuantifierClass(final SootClass clazz) {
    return clazz.getName().equals(QUANTIFIER_CLASS_NAME);
  }

  public static boolean isSpecialClass(final SootClass clazz) {
    return clazz.getName().equals(SPECIAL_CLASS_NAME);
  }

  public static boolean isBindingClass(final SootClass clazz) {
    return clazz.getName().equals(BINDING_CLASS_NAME);
  }

  public static boolean isAnnotationClass(final SootClass clazz) {
    return clazz.getPackageName().equals(ANNOTATION_PACKAGE);
  }

}
