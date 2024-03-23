package byteback.mlcfg.vimp;

import byteback.mlcfg.identifiers.Identifier;
import byteback.mlcfg.identifiers.IdentifierEscaper;
import byteback.mlcfg.syntax.WhyField;
import byteback.mlcfg.syntax.WhyInstanceField;
import byteback.mlcfg.syntax.WhyStaticField;
import byteback.mlcfg.syntax.types.WhyType;
import soot.SootField;

public class VimpFieldParser {

	private final VimpClassNameParser classNameParser;
	private final IdentifierEscaper identifierEscaper;
	private final TypeResolver typeResolver;

	public VimpFieldParser(VimpClassNameParser classNameParser, IdentifierEscaper identifierEscaper,
						   TypeResolver typeResolver) {
		this.classNameParser = classNameParser;
		this.identifierEscaper = identifierEscaper;
		this.typeResolver = typeResolver;
	}

	public WhyField parse(SootField f) {
		final Identifier.FQDN clazz = classNameParser.parse(f.getDeclaringClass());
		final Identifier.U fieldName = identifierEscaper.escapeU(f.getName());
		final WhyType fieldType = typeResolver.resolveType(f.getType());

		return f.isStatic()
				? new WhyStaticField(clazz, fieldName, fieldType)
				: new WhyInstanceField(clazz, fieldName, fieldType);
	}
}
