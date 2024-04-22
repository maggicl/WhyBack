package byteback.whyml.syntax.function;

import byteback.whyml.ListComparator;
import byteback.whyml.identifiers.Identifier;
import java.util.Comparator;
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
						 WhyFunctionKind.Declaration decl,
						 Optional<VimpMethodParamNames> names) implements Comparable<VimpMethod> {
	private static final Comparator<List<Type>> PARAMETERS_ORDER =
			(ListComparator<Type>) (o1, o2) -> o1.toString().compareTo(o2.toString());


	public VimpMethod {
		names.ifPresent(n -> {
			if (thisType.isEmpty() != n.thisName().isEmpty()) {
				throw new IllegalArgumentException("thisTypeName must be present or absent if thisType is present or absent");
			}

			if (parameterTypes.size() != n.parameterNames().size()) {
				throw new IllegalArgumentException("parameterTypes and parameterNames should have same length");
			}
		});
	}

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
				thisType,
				Stream.concat(
								parameterTypes.stream(),
								// if non-void, result added as last parameter
								Stream.ofNullable(returnType != VoidType.v() && hasResult ? returnType : null))
						.toList(),
				BooleanType.v(),
				WhyFunctionKind.Declaration.PREDICATE,
				Optional.empty());
	}

	public WhyFunctionKind kind(WhyFunctionKind.Inline inline) {
		return new WhyFunctionKind(thisType().isPresent(), decl, inline);
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		VimpMethod that = (VimpMethod) o;
		return Objects.equals(className, that.className) &&
				Objects.equals(name, that.name) &&
				Objects.equals(thisType, that.thisType) &&
				Objects.equals(parameterTypes, that.parameterTypes) &&
				Objects.equals(returnType, that.returnType) &&
				decl == that.decl;
	}

	@Override
	public int hashCode() {
		return Objects.hash(className, name, thisType, parameterTypes, returnType, decl);
	}

	@Override
	public int compareTo(VimpMethod o) {
		int a = this.className.compareTo(o.className);
		if (a != 0) return a;
		int b = this.name.compareTo(o.name);
		if (b != 0) return b;
		int c = PARAMETERS_ORDER.compare(this.parameterTypes, o.parameterTypes);
		if (c != 0) return c;
		return this.returnType.toString().compareTo(o.returnType.toString());
	}
}