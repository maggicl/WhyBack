package byteback.whyml.syntax.function;

import byteback.whyml.identifiers.Identifier;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import soot.AbstractJasminClass;
import soot.BooleanType;
import soot.Type;
import soot.VoidType;

public record VimpMethod(Identifier.FQDN className,
						 String name,
						 Optional<Type> thisType,
						 List<Type> parameterTypes,
						 Type returnType,
						 WhyFunctionKind kind) {

	/**
	 * Return a VimpMethod signature for a spec method with the given name matching the signature of this method
	 *
	 * @param name      name of the condition method
	 * @param hasResult true if the `result` special symbol should be available in the condition
	 * @return the matching VimpMethod object
	 */
	public VimpMethod condition(String name, boolean hasResult) {
		return new VimpMethod(
				this.className,
				name,
				// method must be static
				Optional.empty(),
				Stream.concat(
								// this parameter is the first parameter if present
								Stream.concat(thisType.stream(), parameterTypes.stream()),
								// if non-void, result added as last parameter
								Stream.ofNullable(returnType != VoidType.v() && hasResult ? returnType : null))
						.toList(),
				BooleanType.v(),
				WhyFunctionKind.PREDICATE);
	}

	public String descriptor() {
		StringBuilder buffer = new StringBuilder();
		buffer.append('(');

		for (final Type t : parameterTypes) {
			buffer.append(AbstractJasminClass.jasminDescriptorOf(t));
		}

		buffer.append(')');
		buffer.append(AbstractJasminClass.jasminDescriptorOf(returnType));

		return buffer.toString();
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
				&& Objects.equals(thisType, that.thisType)
				&& Objects.equals(parameterTypes, that.parameterTypes)
				&& Objects.equals(returnType, that.returnType)
				&& normalizedKind() == that.normalizedKind();
	}

	@Override
	public int hashCode() {
		return Objects.hash(className, name, thisType, parameterTypes, returnType, normalizedKind());
	}
}