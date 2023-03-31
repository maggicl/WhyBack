package byteback.analysis;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import byteback.analysis.util.SootAnnotations;
import byteback.analysis.util.SootHosts;
import byteback.analysis.util.SootAnnotationElems.ClassElemExtractor;
import soot.Scene;
import soot.SootClass;
import soot.SootMethod;
import soot.tagkit.AnnotationElem;
import soot.tagkit.AnnotationTag;

public class AnnotationsAttacher {

	public static void attachAll(final Scene scene) {
		final Iterator<SootClass> classIterator = scene.getClasses().snapshotIterator();

		while (classIterator.hasNext()) {
			final SootClass attachedClass = classIterator.next();

			if (SootHosts.hasAnnotation(attachedClass, Namespace.ATTACH_ANNOTATION)) {
				final AnnotationTag annotation = SootHosts.getAnnotation(attachedClass, Namespace.ATTACH_ANNOTATION).orElseThrow();
				final AnnotationElem element = SootAnnotations.getElem(annotation, "value").orElseThrow();
				final String value = new ClassElemExtractor().visit(element);
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

}
