package byteback.core.preprocessing;

import java.util.Iterator;

import byteback.core.representation.soot.body.SootExpressionVisitor;
import soot.Local;
import soot.RefType;
import soot.Unit;
import soot.ValueBox;
import soot.grimp.GrimpBody;
import soot.grimp.NewInvokeExpr;
import soot.jimple.AssignStmt;
import soot.jimple.InvokeStmt;
import soot.jimple.Jimple;
import soot.jimple.NewExpr;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.toolkits.scalar.DefaultLocalCreation;
import soot.jimple.toolkits.scalar.LocalCreation;

public class NewInvokeRemover {

	public GrimpBody transform(final GrimpBody source) {
		final GrimpBody body = (GrimpBody) source.clone();
		final Iterator<Unit> iterator = body.getUnits().snapshotIterator();
		final LocalCreation localCreation = new DefaultLocalCreation(source.getLocals(), "$new");

		while (iterator.hasNext()) {
			final Unit unit = iterator.next();

			for (final ValueBox box : unit.getUseBoxes()) {

				box.getValue().apply(new SootExpressionVisitor<>() {

					@Override
					public void caseNewInvokeExpr(final NewInvokeExpr newInvoke) {
						final RefType type = newInvoke.getBaseType();
						final Local local = localCreation.newLocal(type);
						final NewExpr newExpr = Jimple.v().newNewExpr(type);
						final AssignStmt assignStmt = Jimple.v().newAssignStmt(local, newExpr);
						final SpecialInvokeExpr initExpr = Jimple.v()
							.newSpecialInvokeExpr(local, newInvoke.getMethodRef(), newInvoke.getArgs());
						final InvokeStmt initStmt = Jimple.v().newInvokeStmt(initExpr);
						body.getLocals().add(local);
						body.getUnits().insertBefore(assignStmt, unit);
						body.getUnits().insertAfter(initStmt, assignStmt);
						box.setValue(local);
					}

				});
			}
		}

		return body;
	}

}
