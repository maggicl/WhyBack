package byteback.whyml.printer;

import java.util.Optional;

public record WhyClassDeclaration(Statement typeDeclaration, Optional<Statement> fieldDeclaration) {
}