package byteback.core.representation.body.soot;

import java.util.Collection;

import byteback.core.representation.Visitable;
import soot.Body;
import soot.Unit;

public class SootBody implements Visitable<SootStatementVisitor> {

    private final Body sootBody;

    public SootBody(final Body sootBody) {
        this.sootBody = sootBody;
    }

    public Collection<Unit> getUnits() {
        return sootBody.getUnits();
    }

    @Override
    public void apply(final SootStatementVisitor visitor) {
        for (Unit unit : sootBody.getUnits()) {
            unit.apply(visitor);
        }
    }

}
