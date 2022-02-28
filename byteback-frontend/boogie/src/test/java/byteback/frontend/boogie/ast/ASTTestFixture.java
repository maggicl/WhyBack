package byteback.frontend.boogie.ast;

import beaver.Scanner;

import byteback.frontend.boogie.ResourcesUtil;
import byteback.frontend.boogie.parser.BoogieParser;
import byteback.frontend.boogie.scanner.BoogieLexer;

public class ASTTestFixture {

    public Program getProgram(String name) {
        try {
            final BoogieParser parser = new BoogieParser();
            final Scanner scanner = new BoogieLexer(ResourcesUtil.getBoogieReader(name));
            return (Program) parser.parse(scanner);
        } catch (Exception exception) {
            throw new RuntimeException(exception);
        }
    }
}
