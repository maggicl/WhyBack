package byteback.whyml.syntax.function;

import byteback.whyml.identifiers.Identifier;
import java.util.List;
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
						 WhyFunctionKind.Declaration decl) {

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
				thisType,
				Stream.concat(
								parameterTypes.stream(),
								// if non-void, result added as last parameter
								Stream.ofNullable(returnType != VoidType.v() && hasResult ? returnType : null))
						.toList(),
				BooleanType.v(),
				WhyFunctionKind.Declaration.PREDICATE);
	}

	public WhyFunctionKind kind(WhyFunctionKind.Inline inline) {
		return new WhyFunctionKind(
				thisType().isPresent(),
				decl,
				inline
		);
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
}