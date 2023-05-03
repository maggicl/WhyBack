package byteback.analysis.transformer;

import java.util.Map;

import byteback.util.Lazy;
import soot.Body;
import soot.Scene;
import soot.Value;
import soot.grimp.Grimp;
import soot.grimp.GrimpBody;
import soot.jimple.ArrayRef;
import soot.jimple.InstanceFieldRef;
import soot.jimple.InstanceInvokeExpr;
import soot.jimple.LengthExpr;
import soot.jimple.NewExpr;
import soot.jimple.NullConstant;
import soot.jimple.SpecialInvokeExpr;
import soot.jimple.ThisRef;

public class NullCheckTransformer extends CheckTransformer {

	private static final Lazy<NullCheckTransformer> instance = Lazy.from(NullCheckTransformer::new);

	public static NullCheckTransformer v() {
		return instance.get();
	}

	private NullCheckTransformer() {
		super(Scene.v().loadClassAndSupport("java.lang.NullPointerException"));
	}

	@Override
	public void internalTransform(final Body body, final String phaseName, final Map<String, String> options) {
		if (body instanceof GrimpBody) {
			transformBody(body);
		} else {
			throw new IllegalArgumentException("Can only transform Grimp");
		}
	}

	@Override
	public Value extractTarget(final Value value) {
		Value target = null;

		if (value instanceof NewExpr || value instanceof SpecialInvokeExpr || target instanceof ThisRef) {
			return null;
		}

		if (value instanceof InstanceInvokeExpr invokeExpr) {
			target = invokeExpr.getBase();
		}

		if (value instanceof InstanceFieldRef fieldRef) {
			target = fieldRef.getBase();
		}

		if (value instanceof ArrayRef arrayRef) {
			target = arrayRef.getBase();
		}

		if (value instanceof LengthExpr lengthExpr) {
			target = lengthExpr.getOp();
		}

		return target;
	}

	@Override
	public Value makeCheckExpr(Value inner, Value outer) {
		return Grimp.v().newNeExpr(inner, NullConstant.v());
	}

}
