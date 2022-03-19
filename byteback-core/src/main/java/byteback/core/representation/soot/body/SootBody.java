package byteback.core.representation.soot.body;

import java.util.stream.Stream;

import byteback.core.representation.Visitable;
import soot.Body;
import soot.Unit;

public class SootBody implements Visitable<SootStatementVisitor<?>> {

    private final Body sootBody;

    public SootBody(final Body sootBody) {
        this.sootBody = sootBody;
    }

    public Stream<SootStatement> statements() {
        return sootBody.getUnits().stream().map(SootStatement::new);
    }

    public int getStatementCount() {
        return sootBody.getUnits().size();
    }

    @Override
    public void apply(final SootStatementVisitor<?> visitor) {
        for (Unit unit : sootBody.getUnits()) {
            unit.apply(visitor);
        }
    }

    @Override
    public String toString() {
        return sootBody.toString();
    }

}
