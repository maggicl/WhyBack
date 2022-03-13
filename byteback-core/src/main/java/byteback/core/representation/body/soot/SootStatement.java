package byteback.core.representation.body.soot;

import byteback.core.representation.Visitable;

public class SootStatement implements Visitable<SootStatementVisitor<?>> {

    private final soot.Unit sootUnit;

    public SootStatement(final soot.Unit sootUnit) {
        this.sootUnit = sootUnit;
    }

    @Override
    public void apply(final SootStatementVisitor<?> visitor) {
        sootUnit.apply(visitor);
    }

}
