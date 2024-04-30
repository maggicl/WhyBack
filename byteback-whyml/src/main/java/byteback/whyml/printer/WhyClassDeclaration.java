package byteback.whyml.printer;

import java.util.Optional;

public record WhyClassDeclaration(Code typeDeclaration, Optional<Code> fieldDeclaration) {
}