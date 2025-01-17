package byteback.analysis;

import byteback.analysis.tags.PositionTag;
import byteback.analysis.transformer.CallCheckTransformer;
import byteback.analysis.transformer.DynamicToStaticTransformer;
import byteback.analysis.transformer.ExceptionInvariantTransformer;
import byteback.analysis.transformer.ExpressionFolder;
import byteback.analysis.transformer.GuardTransformer;
import byteback.analysis.transformer.IndexCheckTransformer;
import byteback.analysis.transformer.InvariantCheckerTransformer;
import byteback.analysis.transformer.InvariantExpander;
import byteback.analysis.transformer.LogicExpressionFolder;
import byteback.analysis.transformer.LogicUnitTransformer;
import byteback.analysis.transformer.LogicValueTransformer;
import byteback.analysis.transformer.NullCheckTransformer;
import byteback.analysis.transformer.PositionTagTransformer;
import byteback.analysis.transformer.PureTransformer;
import byteback.analysis.transformer.QuantifierValueTransformer;
import byteback.analysis.transformer.SwitchToIfTransformer;
import byteback.analysis.util.SootAnnotations;
import byteback.analysis.util.SootBodies;
import byteback.analysis.util.SootClasses;
import byteback.analysis.util.SootHosts;
import byteback.analysis.util.SootMethods;
import byteback.util.Lazy;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import soot.ArrayType;
import soot.Body;
import soot.RefType;
import soot.Scene;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Trap;
import soot.Type;
import soot.Value;
import soot.ValueBox;
import soot.grimp.Grimp;
import soot.jimple.FieldRef;
import soot.jimple.InstanceOfExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.NewExpr;
import soot.tagkit.AbstractHost;
import soot.tagkit.SourceFileTag;
import soot.tagkit.SourceLnPosTag;
import soot.toolkits.scalar.UnusedLocalEliminator;

public class RootResolver {

	private static final Lazy<RootResolver> instance = Lazy.from(RootResolver::new);
	private final Deque<AbstractHost> next;
	private final Set<AbstractHost> visited;
	private final Map<SootMethod, List<VimpCondition>> conditions;
	private boolean checkArrayDereference;
	private boolean checkNullDereference;
	private boolean makeThrowStmtReturn;
	private boolean transformSwitchToIf;
	private boolean preserveInvariants;

	private RootResolver() {
		this.next = new LinkedList<>();
		this.visited = new HashSet<>();
		this.checkArrayDereference = false;
		this.checkNullDereference = false;
		this.makeThrowStmtReturn = true;
		this.preserveInvariants = false;
		this.transformSwitchToIf = false;
		conditions = new HashMap<>();
	}

	public static RootResolver v() {
		return instance.get();
	}

	public void transformMethod(final SootMethod method) {
		final SootClass clazz = method.getDeclaringClass();
		if (SootMethods.hasBody(method)) {
			if (SootHosts.hasAnnotation(method, Namespace.PRELUDE_ANNOTATION)) {
				return;
			}

			SootBodies.validateCalls(method.retrieveActiveBody());
			final Body body = Grimp.v().newBody(method.getActiveBody(), "");

			// import line numbers if the class file points to the source file
			if (clazz.hasTag("SourceFileTag")) {
				final SourceFileTag tag = (SourceFileTag) clazz.getTag("SourceFileTag");
				String path = tag.getAbsolutePath();
				if (path == null) {
					path = tag.getSourceFile();
				}
				if (path != null) {
					// get line from source
					SourceLnPosTag endTag = (SourceLnPosTag) method.getTag(SourceLnPosTag.NAME);
					int endPos = (endTag == null) ? -1 : endTag.endPos();

					new PositionTagTransformer(path).transform(body);
					method.addTag(new PositionTag(path, method.getJavaSourceStartLineNumber(), method.getJavaSourceStartColumnNumber(), endPos));
				}
			}

			LogicUnitTransformer.v().transform(body);
			new LogicValueTransformer(body.getMethod().getReturnType()).transform(body);

			if (!Namespace.isPureMethod(method) && !Namespace.isPredicateMethod(method)) {
				new LogicExpressionFolder().transform(body);
				QuantifierValueTransformer.v().transform(body);
				ExceptionInvariantTransformer.v().transform(body);

				if (checkArrayDereference || SootHosts.hasAnnotation(method, Namespace.MODEL_IOBE_ANNOTATION)) {
					IndexCheckTransformer.v().transform(body);
				}

				if (checkNullDereference || SootHosts.hasAnnotation(method, Namespace.MODEL_NPE_ANNOTATION)) {
					NullCheckTransformer.v().transform(body);
				}

				CallCheckTransformer.v().transform(body);
				new GuardTransformer(this.makeThrowStmtReturn).transform(body);
				if (this.transformSwitchToIf) SwitchToIfTransformer.v().transform(body);
			} else {
				PureTransformer.v().transform(body);
				new ExpressionFolder().transform(body);
				QuantifierValueTransformer.v().transform(body);
			}

			UnusedLocalEliminator.v().transform(body);
			DynamicToStaticTransformer.v().transform(body);

			if (!Namespace.isPureMethod(method) && !Namespace.isPredicateMethod(method)) {
				if (this.preserveInvariants) {
					InvariantCheckerTransformer.v().transform(body);
				} else {
					InvariantExpander.v().transform(body);
				}
			}

			method.setActiveBody(body);
		}
	}

	public void setCheckArrayDereference(boolean f) {
		checkArrayDereference = f;
	}

	public void setCheckNullDereference(boolean f) {
		checkNullDereference = f;
	}

	public void setMakeThrowStmtReturn(boolean f) {
		makeThrowStmtReturn = f;
	}

	public void setPreserveInvariants(boolean preserveInvariants) {
		this.preserveInvariants = preserveInvariants;
	}

	public void setTransformSwitchToIf(boolean transformSwitchToIf) {
		this.transformSwitchToIf = transformSwitchToIf;
	}

	public boolean classIsValid(final SootClass clazz) {
		return !Namespace.isAnnotationClass(clazz);
	}

	public void addNext(final AbstractHost host) {
		if (!visited.contains(host)) {
			visited.add(host);
			next.add(host);
		}
	}

	public void addType(final Type type) {
		if (type instanceof ArrayType arrayType) {
			addType(arrayType.getElementType());
		}

		if (type instanceof RefType refType) {
			final SootClass clazz = refType.getSootClass();
			addClass(clazz);
		}
	}

	public void addClass(final SootClass clazz) {
		if (classIsValid(clazz)) {
			addNext(clazz);
		}
	}

	public void addMethod(final SootMethod method) {
		if (classIsValid(method.getDeclaringClass())) {
			addNext(method);
		}
	}

	public void addField(final SootField field) {
		if (classIsValid(field.getDeclaringClass())) {
			addNext(field);
		}
	}

	public void scanField(final SootField field) {
		final SootClass declaringClass = field.getDeclaringClass();
		addType(field.getType());
		addClass(declaringClass);
	}

	public void scanSignature(final SootMethod method) {
		for (final Type type : method.getParameterTypes()) {
			addType(type);
		}

		final List<VimpCondition> conditionList = SootHosts.getAnnotations(method)
				.flatMap(SootAnnotations::getAnnotations)
				.flatMap(e -> VimpCondition.parse(method, e).stream())
				.toList();

		// Add all exception classes and predicate methods references by the conditions of this method
		conditions.put(method, conditionList);

		for (final VimpCondition condition : conditionList) {
			condition.getHosts().forEach(this::addNext);
		}

		addType(method.getReturnType());
	}

	public Map<SootMethod, List<VimpCondition>> getConditions() {
		return new HashMap<>(conditions);
	}

	public void scanMethod(final SootMethod method) {
		scanSignature(method);

		if (!SootMethods.hasBody(method)) {
			return;
		}

		transformMethod(method);
		final Body body = method.retrieveActiveBody();

		for (final ValueBox useDefBox : body.getUseAndDefBoxes()) {
			final Value useDef = useDefBox.getValue();
			addType(useDef.getType());

			if (useDef instanceof InvokeExpr invoke) {
				final SootMethod usedMethod = invoke.getMethod();
				final SootClass declaringClass = usedMethod.getDeclaringClass();
				addMethod(usedMethod);
				addClass(declaringClass);
			}

			if (useDef instanceof InstanceOfExpr instanceOfExpr) {
				addType(instanceOfExpr.getCheckType());
			}

			if (useDef instanceof NewExpr newExpr) {
				addType(newExpr.getBaseType());
			}

			if (useDef instanceof FieldRef fieldRef) {
				addField(fieldRef.getField());
			}
		}

		for (final Trap trap : body.getTraps()) {
			addClass(trap.getException());
		}
	}

	public void scanClass(final SootClass clazz) {
		if (clazz.hasSuperclass()) {
			addClass(clazz.getSuperclass());
		}

		for (final SootClass intf : clazz.getInterfaces()) {
			addClass(intf);
		}

		if (!SootClasses.isBasicClass(clazz)) {
			for (final SootMethod method : clazz.getMethods()) {
				addMethod(method);
			}

			for (final SootField field : clazz.getFields()) {
				addField(field);
			}
		}
	}

	public Iterable<SootClass> getUsedClasses() {
		return visited.stream().filter((v) -> v instanceof SootClass).map((v) -> (SootClass) v)::iterator;
	}

	public Iterable<SootMethod> getUsedMethods() {
		return visited.stream().filter((v) -> v instanceof SootMethod).map((v) -> (SootMethod) v)::iterator;
	}

	public Iterable<SootField> getUsedFields() {
		return visited.stream().filter((v) -> v instanceof SootField).map((v) -> (SootField) v)::iterator;
	}

	public void resolve(final Collection<SootClass> initials) {
		for (final SootClass initial : initials) {
			addClass(initial);

			for (SootMethod method : initial.getMethods()) {
				addMethod(method);
			}
		}

		resolveAll();
	}

	public void resolveAll() {
		while (!next.isEmpty()) {
			final AbstractHost current = next.pollFirst();
			scan(current);
		}
	}

	public void scan(final AbstractHost host) {
		if (host instanceof SootClass clazz) {
			scanClass(clazz);
		} else if (host instanceof SootMethod method) {
			scanMethod(method);
		} else if (host instanceof SootField field) {
			scanField(field);
		}
	}

	public List<SootClass> getVisibleSubclassesOf(final SootClass clazz) {
		final Collection<SootClass> subclasses = Scene.v().getOrMakeFastHierarchy().getSubclassesOf(clazz);

		return subclasses.stream().filter(visited::contains).toList();
	}

	public void ensureResolved(final AbstractHost host) {
		if (!visited.contains(host)) {
			scan(host);
			resolveAll();
		}
	}

}
