package byteback.core.converter.soot.boogie;

import java.util.Iterator;
import byteback.core.representation.unit.soot.SootMethodUnit;
import byteback.core.representation.type.soot.SootType;

public class BoogieNameConverter {

    static String methodName(SootMethodUnit methodUnit) {
        final StringBuilder builder = new StringBuilder();
        final Iterator<SootType> typeIterator = methodUnit.getParameterTypes().iterator();
        builder.append(methodUnit.getClassUnit().getName());
        builder.append(".");
        builder.append(methodUnit.getName());
        builder.append("#");

        while (typeIterator.hasNext()) {
            builder.append(typeIterator.next());
            builder.append("#");
        }

        if (methodUnit.getParameterTypes().size() == 0) {
            builder.append("#");
        }

        return builder.toString();
    }
    
}
