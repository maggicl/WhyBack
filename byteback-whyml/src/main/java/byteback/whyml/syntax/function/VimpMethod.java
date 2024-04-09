package byteback.whyml.syntax.function;

import byteback.whyml.identifiers.Identifier;
import java.util.Objects;

public record VimpMethod(Identifier.FQDN className,
						 String name,
						 String descriptor,
						 WhyFunctionKind kind) {

	public String condDescriptor() {
		return descriptor
				.replaceAll("\\).+$", ")Z"); // change return type to boolean
	}

	public String resultCondDescriptor() {
		return descriptor
				.replaceAll("\\)V$", "") // if void, result parameter not included
				.replaceAll("\\)", "") + ")Z"; // if non-void, result added as last parameter
	}

	// hack to make PURE_PREDICATE and PREDICATE equivalent
	private WhyFunctionKind normalizedKind() {
		return kind == WhyFunctionKind.PURE_PREDICATE ? WhyFunctionKind.PREDICATE : kind;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		VimpMethod that = (VimpMethod) o;
		return Objects.equals(className, that.className)
				&& Objects.equals(name, that.name)
				&& Objects.equals(descriptor, that.descriptor)
				&& normalizedKind() == that.normalizedKind();
	}

	@Override
	public int hashCode() {
		return Objects.hash(className, name, descriptor, normalizedKind());
	}
}