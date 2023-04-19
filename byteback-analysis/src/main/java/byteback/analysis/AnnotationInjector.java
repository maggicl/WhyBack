package byteback.analysis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import byteback.analysis.util.SootAnnotations;
import byteback.analysis.util.SootHosts;
import byteback.analysis.util.SootAnnotationElems.ClassElemExtractor;
import byteback.analysis.util.SootAnnotationElems.StringElemExtractor;
import byteback.util.Lazy;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.tagkit.AnnotationElem;
import soot.tagkit.AnnotationTag;
import soot.util.Chain;

public class AnnotationInjector {

	private static final Lazy<AnnotationInjector> instance = Lazy.from(AnnotationInjector::new);

	public static AnnotationInjector v() {
		return instance.get();
	}

	public void inject(final Chain<SootClass> classes) {
		final Iterator<SootClass> classIterator = classes.snapshotIterator();

		while (classIterator.hasNext()) {
			final SootClass attachedClass = classIterator.next();
			final AnnotationTag annotation;
			final AnnotationElem element;
			final String value;

			if (SootHosts.hasAnnotation(attachedClass, Namespace.ATTACH_ANNOTATION)) {
				annotation = SootHosts.getAnnotation(attachedClass, Namespace.ATTACH_ANNOTATION).orElseThrow();
				element = SootAnnotations.getElem(annotation, "value").orElseThrow();
				value = new ClassElemExtractor().visit(element);

				System.out.println(value);
			} else if(SootHosts.hasAnnotation(attachedClass, Namespace.ATTACH_LABEL_ANNOTATION)) {
				annotation = SootHosts.getAnnotation(attachedClass, Namespace.ATTACH_LABEL_ANNOTATION).orElseThrow();
				element = SootAnnotations.getElem(annotation, "value").orElseThrow();
				value = new StringElemExtractor().visit(element);

				System.out.println(value);
			} else {
				continue;
			}
			
			final SootClass hostClass = Scene.v().loadClassAndSupport(Namespace.stripDescriptor(value));
			hostClass.addAllTagsOf(attachedClass);

			final List<SootMethod> methodsSnapshot = new ArrayList<>(attachedClass.getMethods());

			for (final SootMethod attachedMethod : methodsSnapshot) {
				final SootMethod hostMethod = hostClass.getMethodUnsafe(attachedMethod.getNumberedSubSignature());

				attachedClass.removeMethod(attachedMethod);
				attachedMethod.setDeclared(false);

				if (hostMethod != null) {
					hostClass.removeMethod(hostMethod);
				}

				hostClass.addMethod(attachedMethod);
			}
		}
	}
}
