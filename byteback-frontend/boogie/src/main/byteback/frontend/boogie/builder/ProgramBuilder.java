package byteback.frontend.boogie.builder;

import byteback.frontend.boogie.ast.Program;

public class ProgramBuilder {

    final Program program;

    public ProgramBuilder() {
        this.program = new Program();
    }

    public ProgramBuilder addDeclaration(final Declaration declaration) {
        program.addDeclaration(declaration);

        return this;
    }

    public Program build() {
        return program;
    }

}
