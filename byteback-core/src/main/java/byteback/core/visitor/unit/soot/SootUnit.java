package byteback.core.visitor.unit.soot;

import byteback.core.visitor.unit.Unit;

public class SootUnit implements Unit<SootUnitVisitor> {

    private final soot.Unit sootUnit;

    public SootUnit(final soot.Unit sootUnit) {
        this.sootUnit = sootUnit;
    }

    @Override
    public void apply(final SootUnitVisitor visitor) {
        sootUnit.apply(visitor);
    }

}
