package byteback.analysis;

import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import byteback.analysis.util.SootMethods;
import byteback.util.Lazy;
import byteback.analysis.util.SootClasses;
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
import soot.jimple.FieldRef;
import soot.jimple.InstanceOfExpr;
import soot.jimple.InvokeExpr;
import soot.jimple.NewExpr;
import soot.tagkit.AbstractHost;

public class RootResolver {

	private static final Lazy<RootResolver> instance = Lazy.from(RootResolver::new);

	private final Deque<AbstractHost> next;

	private final Set<AbstractHost> visited;

	public static RootResolver v() {
		return instance.get();
	}

	private RootResolver() {
		this.next = new LinkedList<>();
		this.visited = new HashSet<>();
	}

	public boolean classIsValid(final SootClass clazz)  {
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

		addType(method.getReturnType());
	}

	public void scanMethod(final SootMethod method) {
		if (!SootMethods.hasBody(method)) {
			scanSignature(method);
			return;
		}

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
		return visited.stream().filter((v) -> v instanceof SootClass)
			.map((v) -> (SootClass) v)::iterator;
	}

	public Iterable<SootMethod> getUsedMethods() {
		return visited.stream().filter((v) -> v instanceof SootMethod)
			.map((v) -> (SootMethod) v)::iterator;
	}

	public Iterable<SootField> getUsedFields() {
		return visited.stream().filter((v) -> v instanceof SootField)
			.map((v) -> (SootField) v)::iterator;
	}

	public void resolve(final Collection<SootClass> initials) {
		for (final SootClass initial : initials) {
			addClass(initial);

			for (SootMethod method : initial.getMethods()) {
				addMethod(method);
			}
		}

		while (!next.isEmpty()) {
			final AbstractHost current = next.pollFirst();

			if (current instanceof SootClass clazz) {
				scanClass(clazz);
			} else if (current instanceof SootMethod method) {
				scanMethod(method);
			} else if (current instanceof SootField field) {
				scanField(field);
			}
		}
	}

	public List<SootClass> getVisibleSubclassesOf(final SootClass clazz) {
		final Collection<SootClass> subclasses = Scene.v().getOrMakeFastHierarchy().getSubclassesOf(clazz);

		return subclasses.stream().filter((c) -> visited.contains(c)).toList();
	}

}
