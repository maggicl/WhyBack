package byteback.mlcfg.syntax;

import byteback.frontend.boogie.ast.Printable;

public record WhyProgram(String program) implements Printable {
    @Override
    public void print(StringBuilder builder) {
        builder.append(program).append('\n');
    }
}