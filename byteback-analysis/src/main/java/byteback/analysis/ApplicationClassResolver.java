package byteback.analysis;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.Set;

import byteback.analysis.util.SootClasses;
import soot.Body;
import soot.RefType;
import soot.SootClass;
import soot.SootField;
import soot.SootMethod;
import soot.Type;
import soot.Value;
import soot.ValueBox;
import soot.jimple.InvokeExpr;

public class ApplicationClassResolver {

	private final Deque<SootClass> next;

	private final Set<SootClass> applicationClasses;

	private final Set<SootMethod> applicationMethods;

	public ApplicationClassResolver() {
		this.next = new ArrayDeque<>();
		this.applicationClasses = new HashSet<>();
		this.applicationMethods = new HashSet<>();
	}

	public void resolveClass(final SootClass clazz) {
		if (!clazz.isApplicationClass()) {
			applicationClasses.add(clazz);
			next.add(clazz);
			clazz.setApplicationClass();
		}
	}

	public void resolveMethod(final SootMethod method) {
		applicationMethods.add(method);

		if (SootClasses.isBasicClass(method.getDeclaringClass())) {
			method.getDeclaringClass().setPhantomClass();
			method.setPhantom(true);
		}
	}

	public void resolveType(final Type type) {
		if (type instanceof RefType refType && refType.hasSootClass()) {
			final SootClass clazz = refType.getSootClass();
			resolveClass(clazz);
		}
	}

	public void resolveBodies(final SootClass clazz) {
		for (final SootMethod method : clazz.getMethods()) {
			if (method.hasActiveBody()) {
				final Body body = method.getActiveBody();

				for (final ValueBox vbox : body.getUseAndDefBoxes()) {
					final Value value = vbox.getValue();
					resolveType(value.getType());

					if (value instanceof InvokeExpr invokeExpr) {
						resolveMethod(invokeExpr.getMethod());
					}
				}
			}
		}
	}

	public void resolveHierarchy(final SootClass clazz) {
		if (clazz.hasSuperclass()) {
			resolveClass(clazz.getSuperclass());
		}

		for (final SootClass interf : clazz.getInterfaces()) {
			resolveClass(interf);
		}
	}

	public void resolveSignatures(final SootClass clazz) {
		for (final SootMethod method : clazz.getMethods()) {
			resolveMethod(method);

			for (final Type type : method.getParameterTypes()) {
				resolveType(type);
			}

			resolveType(method.getReturnType());
		}
	}

	public void resolveFields(final SootClass clazz) {
		for (final SootField field : clazz.getFields()) {
			resolveType(field.getType());
		}
	}

	public void resolve(final Collection<SootClass> initials) {
		next.addAll(initials);

		while (!next.isEmpty()) {
			final SootClass current = next.pollFirst();
			final int resolvingLevel = current.resolvingLevel();

			if (resolvingLevel >= SootClass.HIERARCHY) {
				resolveHierarchy(current);
			}

			if (resolvingLevel >= SootClass.SIGNATURES) {
				resolveFields(current);
				resolveSignatures(current);
			}

			if (resolvingLevel >= SootClass.BODIES) {
				resolveBodies(current);
			}
		}
	}

}
