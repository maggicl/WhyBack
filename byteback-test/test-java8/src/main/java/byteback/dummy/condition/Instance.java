package byteback.dummy.condition;

import static byteback.annotations.Contract.*;

public class Instance {

	public static void isObject() {
		Object object = new Object();

		assertion(object instanceof Object);
	}

}
