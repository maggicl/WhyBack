package byteback.frontend.boogie.ast;

import org.junit.Test;

public class NameAnalysisTest extends ASTTestFixture {

    @Test
    public void ProgramLookupVariable_GivenUnitProgram_ReturnsVariable() {
        final Program program = getProgram("Unit");

        assert(program.programLookupVariable("unit") != null);
    }

    @Test
    public void DeclarationLookupVariable_GivenUnitIdentityFunction_ReturnsVariable() {
        final Program program = getProgram("Unit");
        final FunctionDeclaration functionDeclaration = program.lookupFunctionDeclaration("identity");

        assert(functionDeclaration.declarationLookupVariable("arg") != null);
    }

}
