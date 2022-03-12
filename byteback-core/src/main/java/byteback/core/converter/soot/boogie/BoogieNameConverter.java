package byteback.core.converter.soot.boogie;

import java.util.Iterator;
import byteback.core.representation.unit.soot.SootMethodUnit;
import byteback.core.representation.type.soot.SootType;

public class BoogieNameConverter {

    static String convertMethod(SootMethodUnit methodUnit) {
        final StringBuilder builder = new StringBuilder();
        final Iterator<SootType> typeIterator = methodUnit.getParameterTypes().iterator();
        builder.append(methodUnit.getClassUnit().getName());
        builder.append("#");

        while (typeIterator.hasNext()) {
            builder.append(typeIterator.next().toString());
            builder.append("#");
        }

        return builder.toString();
    }
    
}