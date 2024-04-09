package byteback.mlcfg.vimp;

import byteback.mlcfg.identifiers.Identifier;
import byteback.mlcfg.syntax.WhyFunctionKind;
import java.util.Objects;

public record VimpFunctionReference(Identifier.FQDN className,
									String methodName,
									String descriptor,
									WhyFunctionKind kind) {

	// hack to make PURE_PREDICATE and PREDICATE equivalent
	private WhyFunctionKind normalizedKind() {
		return kind == WhyFunctionKind.PURE_PREDICATE ? WhyFunctionKind.PREDICATE : kind;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		VimpFunctionReference that = (VimpFunctionReference) o;
		return Objects.equals(className, that.className)
				&& Objects.equals(methodName, that.methodName)
				&& Objects.equals(descriptor, that.descriptor)
				&& normalizedKind() == that.normalizedKind();
	}

	@Override
	public int hashCode() {
		return Objects.hash(className, methodName, descriptor, normalizedKind());
	}
}