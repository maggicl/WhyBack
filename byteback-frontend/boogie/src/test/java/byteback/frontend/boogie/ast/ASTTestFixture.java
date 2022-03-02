package byteback.frontend.boogie.ast;

import beaver.Scanner;
import byteback.frontend.boogie.ResourcesUtil;
import byteback.frontend.boogie.parser.BoogieParser;
import byteback.frontend.boogie.scanner.BoogieLexer;

public class ASTTestFixture {

    public Program getProgram(String programName) {
        try {
            final BoogieParser parser = new BoogieParser();
            final Scanner scanner = new BoogieLexer(ResourcesUtil.getBoogieReader(programName));
            return (Program) parser.parse(scanner);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Function getFunction(final String programName, final String functionName) {
        Program program = getProgram(programName);
        Function function = program.lookupFunction(functionName)
            .orElseThrow(() -> new RuntimeException("No such function: " + functionName));

        return function;
    }

    public Procedure getProcedure(final String programName, final String procedureName) {
        Program program = getProgram(programName);
        Procedure procedure = program.lookupProcedure(procedureName)
            .orElseThrow(() -> new RuntimeException("No such procedure: " + procedureName));

        System.out.println(procedure);
        return procedure;
    }

}
