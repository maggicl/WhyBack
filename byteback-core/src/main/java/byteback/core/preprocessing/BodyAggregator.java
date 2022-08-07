package byteback.core.preprocessing;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicBoolean;

import byteback.core.converter.soottoboogie.method.procedure.DefinitionsCollector;
import byteback.core.representation.soot.body.SootExpressionVisitor;
import byteback.core.representation.soot.body.SootStatementVisitor;
import soot.Body;
import soot.Local;
import soot.Unit;
import soot.Value;
import soot.ValueBox;
import soot.grimp.Grimp;
import soot.grimp.GrimpBody;
import soot.jimple.ArrayRef;
import soot.jimple.AssignStmt;
import soot.jimple.InstanceFieldRef;
import soot.jimple.StaticFieldRef;
import soot.util.Chain;

public class BodyAggregator {

	final DefinitionsCollector defCollector;

	public BodyAggregator() {
		this.defCollector = new DefinitionsCollector();
	}

	public void removeUnusedLocals(final Body body) {
		final Chain<Local> locals = body.getLocals();
		final Iterator<Local> iterator = body.getLocals().snapshotIterator();

		defCollector.collect(body);

		while (iterator.hasNext()) {
			final Local local = iterator.next();

			if (defCollector.usesOf(local).size() == 0) {
				locals.remove(local);
			}
		}
	}

	public boolean hasLoop(final Local local, final Unit unit) {
		final Stack<Local> next = new Stack<>();
		final HashSet<Local> visited = new HashSet<>();

		next.push(local);

		while (!next.isEmpty()) {
			final Local current = next.pop();
			final Set<Unit> uses = defCollector.usesOf(current);
			visited.add(current);

			for (Unit use : uses) {
				final AtomicBoolean looping = new AtomicBoolean(false);

				use.apply(new SootStatementVisitor<>() {

					@Override
					public void caseAssignStmt(final AssignStmt assignment) {
						assignment.getLeftOp().apply(new SootExpressionVisitor<>() {

							private void addNext(final Value value) {
								if (!visited.contains(local)) {
									next.push(local);
								} else {
									looping.set(true);
								}
							}

							@Override
							public void caseLocal(final Local local) {
								addNext(local);
							}

							@Override
							public void caseArrayRef(final ArrayRef reference) {
								addNext(reference);
							}

							@Override
							public void caseStaticFieldRef(final StaticFieldRef reference) {
								addNext(reference);
							}

							@Override
							public void caseInstanceFieldRef(final InstanceFieldRef reference) {
								addNext(reference);
							}

						});
					}

				});

				if (looping.get()) {
					return true;
				}
			}
		}

		return false;
	}

	public GrimpBody transform(final Body source) {
		final GrimpBody body = Grimp.v().newBody(source, "gb");
		Iterator<Unit> iterator = body.getUnits().snapshotIterator();
		defCollector.collect(source);

		while (iterator.hasNext()) {
			final Unit unit = iterator.next();

			for (final ValueBox box : unit.getUseBoxes()) {
				final Value value = box.getValue();

				value.apply(new SootExpressionVisitor<>() {

					@Override
					public void caseLocal(final Local local) {
						final Set<Unit> definitions = defCollector.definitionsOf(local);
						final Set<Unit> uses = defCollector.usesOf(local);
						if (definitions.size() == 1 && uses.size() == 1 && !hasLoop(local, unit)) {
							definitions.iterator().next().apply(new SootStatementVisitor<>() {

								@Override
								public void caseAssignStmt(final AssignStmt assignment) {
									body.getUnits().remove(definitions.iterator().next());
									box.setValue(assignment.getRightOp());
								}

							});
						}
					}

				});
			}
		}

		removeUnusedLocals(body);

		return body;
	}

}
