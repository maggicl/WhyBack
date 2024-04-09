package byteback.mlcfg.vimp;

import byteback.mlcfg.identifiers.Identifier;
import byteback.mlcfg.syntax.WhyFunctionKind;

public record VimpFunctionReference(Identifier.FQDN className,
									String methodName,
									String descriptor,
									WhyFunctionKind kind) {
}