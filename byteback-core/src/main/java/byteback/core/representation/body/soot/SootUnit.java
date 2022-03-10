package byteback.core.representation.body.soot;

import byteback.core.Visitable;

public class SootUnit implements Visitable<SootUnitVisitor> {

    private final soot.Unit sootUnit;

    public SootUnit(final soot.Unit sootUnit) {
        this.sootUnit = sootUnit;
    }

    @Override
    public void apply(final SootUnitVisitor visitor) {
        sootUnit.apply(visitor);
    }

}
