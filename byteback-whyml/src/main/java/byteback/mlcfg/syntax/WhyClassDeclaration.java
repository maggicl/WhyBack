package byteback.mlcfg.syntax;

import byteback.mlcfg.identifiers.Identifier;
import java.util.List;

public record WhyClassDeclaration(Identifier.FQDN name, List<WhyFieldDeclaration> fields) {
}
