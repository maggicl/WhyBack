package byteback.analysis;

import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import byteback.analysis.util.SootClasses;
import soot.ArrayType;
import soot.Body;
import soot.RefType;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Value;
import soot.ValueBox;
import soot.jimple.FieldRef;
import soot.jimple.InvokeExpr;

public class ApplicationClassResolver {

	private final Deque<SootClass> next;

	private final Set<SootClass> classes;

	private final Set<SootMethod> usedMethods;

	private final Set<SootField> usedFields;

	private final Set<SootClass> usedClasses;

	public ApplicationClassResolver() {
		this.next = new LinkedList<>();
		this.classes = new HashSet<>();
		this.usedMethods = new HashSet<>();
		this.usedFields = new HashSet<>();
		this.usedClasses = new HashSet<>();
	}

	public void addNext(final SootClass clazz) {
		if (!classes.contains(clazz)) {
			next.add(clazz);
		}
	}

	public void scanType(final Type type) {
		if (type instanceof ArrayType arrayType) {
			scanType(arrayType.getElementType());
		}

		if (type instanceof RefType refType) {
			final SootClass clazz = refType.getSootClass();

			addNext(clazz);
		}
	}

	public void scanField(final SootField field) {
		scanType(field.getType());
	}

	public void scanSignature(final SootMethod method) {
		for (final Type type : method.getParameterTypes()) {
			scanType(type);
		}

		scanType(method.getReturnType());
	}

	public void scanMethod(final SootMethod method) {
		if (method.getDeclaringClass().resolvingLevel() < SootClass.BODIES ||
				SootClasses.isBasicClass(method.getDeclaringClass())) {
			scanSignature(method);
			return;
		}

		usedMethods.add(method);

		final Body body = method.retrieveActiveBody();

		for (final ValueBox useDefBox : body.getUseAndDefBoxes()) {
			final Value useDef = useDefBox.getValue();

			scanType(useDef.getType());

			if (useDef instanceof InvokeExpr invoke) {
				usedMethods.add(invoke.getMethod());
			}

			if (useDef instanceof FieldRef fieldRef) {
				addNext(fieldRef.getField().getDeclaringClass());
				usedFields.add(fieldRef.getField());
			}
		}
	}

	public void scanClass(final SootClass clazz) {
		classes.add(clazz);

		if (clazz.hasSuperclass()) {
			addNext(clazz.getSuperclass());
		}

		for (final SootClass intf : clazz.getInterfaces()) {
			addNext(intf);
		}

		for (final SootField field : clazz.getFields()) {
			scanField(field);
		}

		for (final SootMethod method : clazz.getMethods()) {
			scanMethod(method);
		}
	}

	public Set<SootClass> getClasses() {
		return classes;
	}

	public boolean isUsed(final SootMethod method) {
		return usedMethods.contains(method);
	}

	public boolean isUsed(final SootField field) {
		return usedFields.contains(field);
	}

	public void resolve(final Collection<SootClass> initials) {
		next.addAll(initials);

		while (!next.isEmpty()) {
			final SootClass current = next.pollFirst();
			scanClass(current);
		}
	}

}
