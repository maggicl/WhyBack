package byteback.core.converter.soot.boogie;

import byteback.frontend.boogie.ast.BooleanLiteral;
import soot.jimple.IntConstant;

public class BoogieBooleanExpressionExtractor extends BoogieExpressionExtractor {

    @Override
    public void caseIntConstant(final IntConstant intConstant) {
        if (intConstant.value == 0) {
            setExpression(new BooleanLiteral("false"));
        } else {
            setExpression(new BooleanLiteral("true"));
        }
    }

}
