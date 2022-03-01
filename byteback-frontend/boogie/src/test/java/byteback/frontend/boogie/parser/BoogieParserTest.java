package byteback.frontend.boogie.parser;

import org.junit.Test;

import beaver.Scanner;
import byteback.frontend.boogie.ResourcesUtil;
import byteback.frontend.boogie.ast.FunctionDeclaration;
import byteback.frontend.boogie.ast.Program;
import byteback.frontend.boogie.scanner.BoogieLexer;

public class BoogieParserTest {

    @Test
    public void Parse_GivenUnitProgram_DoesNotThrowExceptions() throws Exception {
        final BoogieParser parser = new BoogieParser();
        final Scanner scanner = new BoogieLexer(ResourcesUtil.getBoogieReader("Unit"));
        Program program = (Program) parser.parse(scanner);

        System.out.println(((FunctionDeclaration)program.getDeclaration(2)).getFunction().variables().get(0).isMutable());
    }

}
