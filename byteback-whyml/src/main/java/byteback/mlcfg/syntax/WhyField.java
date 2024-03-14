package byteback.mlcfg.syntax;

import byteback.mlcfg.identifiers.Identifier;
import byteback.mlcfg.syntax.types.WhyType;

public record WhyField(Identifier.U name, WhyType type, boolean isStatic) {
}