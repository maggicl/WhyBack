package byteback.core.converter.soottoboogie.type;

import byteback.core.converter.soottoboogie.Prelude;
import byteback.core.representation.soot.type.SootType;
import byteback.core.representation.soot.type.SootTypeVisitor;
import byteback.core.representation.soot.unit.SootClass;
import byteback.frontend.boogie.ast.FunctionReference;
import byteback.frontend.boogie.ast.SymbolicReference;
import byteback.frontend.boogie.ast.ValueReference;
import soot.ArrayType;
import soot.RefType;

public class TypeReferenceExtractor extends SootTypeVisitor<SymbolicReference> {

	public SymbolicReference typeReference;

	@Override
	public void caseRefType(final RefType referenceType) {
		typeReference = ValueReference.of(ReferenceTypeConverter.typeName(new SootClass(referenceType.getSootClass())));
	}

	@Override
	public void caseArrayType(final ArrayType arrayType) {
		final FunctionReference arrayTypeReference = Prelude.instance().getArrayTypeFunction().makeFunctionReference();
		SymbolicReference innerTypeReference = visit(new SootType(arrayType.baseType));

		if (innerTypeReference == null) {
			innerTypeReference = Prelude.instance().getPrimitiveTypeConstant().makeValueReference();
		}

		arrayTypeReference.addArgument(innerTypeReference);

		typeReference = arrayTypeReference;
	}

	public SymbolicReference result() {
		return typeReference;
	}

}
