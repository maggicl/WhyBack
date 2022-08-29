package byteback.core.representation.soot.type;

import soot.IntType;
import soot.Scene;
import soot.Type;

public class SootType {

	public static Type join(Type a, Type b) {
		if (a != b) {
			if (Type.toMachineType(a) == Type.toMachineType(a)) {
				return IntType.v();
			} else {
				return a.merge(b, Scene.v());
			}
		}

		return a;
	}

}
