package byteback.core.converter.soottoboogie;

import beaver.Parser;
import byteback.core.util.Lazy;
import byteback.frontend.boogie.ast.Assignee;
import byteback.frontend.boogie.ast.AssignmentStatement;
import byteback.frontend.boogie.ast.BooleanType;
import byteback.frontend.boogie.ast.DefinedType;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.FrameCondition;
import byteback.frontend.boogie.ast.Function;
import byteback.frontend.boogie.ast.FunctionReference;
import byteback.frontend.boogie.ast.IntegerType;
import byteback.frontend.boogie.ast.Label;
import byteback.frontend.boogie.ast.List;
import byteback.frontend.boogie.ast.Procedure;
import byteback.frontend.boogie.ast.Program;
import byteback.frontend.boogie.ast.RealType;
import byteback.frontend.boogie.ast.Statement;
import byteback.frontend.boogie.ast.Type;
import byteback.frontend.boogie.ast.TypeAccess;
import byteback.frontend.boogie.ast.TypeDefinition;
import byteback.frontend.boogie.ast.UnknownTypeAccess;
import byteback.frontend.boogie.ast.ValueReference;
import byteback.frontend.boogie.ast.Variable;
import byteback.frontend.boogie.builder.BoundedBindingBuilder;
import byteback.frontend.boogie.util.ParserUtil;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Prelude {

	private static final Logger log = LoggerFactory.getLogger(Prelude.class);

	private static final Lazy<Program> preamble = Lazy.from(Prelude::initializeProgram);

	public static Program loadProgram() {
		return preamble.get();
	}

	public static Program initializeProgram() {
		final ClassLoader loader = Thread.currentThread().getContextClassLoader();
		final InputStream stream = loader.getResourceAsStream("boogie/BytebackPrelude.bpl");
		assert stream != null;
		final var reader = new InputStreamReader(stream);

		try {
			return ParserUtil.parseBoogieProgram(reader);
		} catch (final IOException exception) {
			log.error("Exception while opening the preamble");
			throw new RuntimeException(exception);
		} catch (final Parser.Exception exception) {
			log.error("Exception while parsing the preamble");
			throw new RuntimeException(exception);
		}
	}

	public static Type getReferenceType() {
		final TypeDefinition typeDefinition = loadProgram().lookupTypeDefinition("Reference")
				.orElseThrow(() -> new IllegalStateException("Missing definition for Reference type"));

		return typeDefinition.getType();
	}

	public static Type getHeapType() {
		final TypeDefinition typeDefinition = loadProgram().lookupTypeDefinition("Store")
				.orElseThrow(() -> new IllegalStateException("Missing definition for heap Store type"));

		return typeDefinition.getType();
	}

	public static DefinedType getFieldType() {
		final TypeDefinition typeDefinition = loadProgram().lookupTypeDefinition("Field")
				.orElseThrow(() -> new IllegalStateException("Missing definition for Field type"));

		return typeDefinition.getType();
	}

	public static Type getBooleanType() {
		return BooleanType.instance();
	}

	public static Type getIntegerType() {
		return IntegerType.instance();
	}

	public static Type getRealType() {
		return RealType.instance();
	}

	public static Variable getHeapVariable() {
		return loadProgram().lookupLocalVariable("~heap")
				.orElseThrow(() -> new IllegalStateException("Missing definition for the ~heap function"));
	}

	public static Variable getNullConstant() {
		return loadProgram().lookupLocalVariable("~null")
				.orElseThrow(() -> new IllegalStateException("Missing definition for the ~null function"));
	}

	public static Function getHeapAccessFunction() {
		return loadProgram().lookupFunction("~read")
				.orElseThrow(() -> new IllegalStateException("Missing definition for the ~read function"));
	}

	public static Function getArrayLengthFunction() {
		return loadProgram().lookupFunction("~lengthof")
				.orElseThrow(() -> new IllegalStateException("Missing definition for the ~lengthof function"));
	}

	public static Function getHeapUpdateFunction() {
		return loadProgram().lookupFunction("~update")
				.orElseThrow(() -> new IllegalStateException("Missing definition for the ~update function"));
	}

	public static Function getArrayAccessFunction() {
		return loadProgram().lookupFunction("~get")
				.orElseThrow(() -> new IllegalStateException("Missing definition for the ~getfunction"));
	}

	public static Function getArrayUpdateFunction() {
		return loadProgram().lookupFunction("~insert")
				.orElseThrow(() -> new IllegalStateException("Missing definition for the ~insert"));
	}

	public static Procedure getNewProcedure() {
		return loadProgram().lookupProcedure("~new")
				.orElseThrow(() -> new IllegalStateException("Missing definition for the ~new procedure"));
	}

	public static Expression getHeapAccessExpression(final Expression base, final Expression field) {
		final FunctionReference reference = getHeapAccessFunction().makeFunctionReference();
		reference.addArgument(getHeapVariable().makeValueReference());
		reference.addArgument(base);
		reference.addArgument(field);

		return reference;
	}

	public static Expression getLengthAccessExpression(final Expression array) {
		final FunctionReference reference = getArrayLengthFunction().makeFunctionReference();
		reference.addArgument(getHeapVariable().makeValueReference());
		reference.addArgument(array);

		return reference;
	}

	public static Variable getArrayTypeVariable(final TypeAccess typeAccess) {
		final String arrayIdentifier = "~Array." + typeAccess.getIdentifier();
		return loadProgram().lookupLocalVariable(arrayIdentifier).orElseThrow(
				() -> new IllegalStateException("Missing definition for array variable " + arrayIdentifier));
	}

	public static Expression getArrayAccessExpression(final TypeAccess typeAccess, final Expression base,
			final Expression index) {
		final FunctionReference reference = getArrayAccessFunction().makeFunctionReference();
		final Variable arrayType = getArrayTypeVariable(typeAccess);
		reference.addArgument(getHeapVariable().makeValueReference());
		reference.addArgument(base);
		reference.addArgument(arrayType.makeValueReference());
		reference.addArgument(index);

		return reference;
	}

	public static Statement getHeapUpdateStatement(final Expression base, final Expression field,
			final Expression value) {
		final FunctionReference updateReference = getHeapUpdateFunction().makeFunctionReference();
		final ValueReference heapReference = getHeapVariable().makeValueReference();
		final var heapAssignee = Assignee.of(heapReference);
		updateReference.addArgument(heapReference);
		updateReference.addArgument(base);
		updateReference.addArgument(field);
		updateReference.addArgument(value);

		return new AssignmentStatement(heapAssignee, updateReference);
	}

	public static Statement getArrayUpdateStatement(final TypeAccess typeAccess, final Expression base,
			final Expression index, final Expression value) {
		final FunctionReference updateReference = getArrayUpdateFunction().makeFunctionReference();
		final ValueReference heapReference = getHeapVariable().makeValueReference();
		final Variable arrayType = getArrayTypeVariable(typeAccess);
		final var heapAssignee = Assignee.of(heapReference);
		updateReference.addArgument(heapReference);
		updateReference.addArgument(base);
		updateReference.addArgument(arrayType.makeValueReference());
		updateReference.addArgument(index);
		updateReference.addArgument(value);

		return new AssignmentStatement(heapAssignee, updateReference);
	}

	public static TypeAccess getFieldTypeAccess(final TypeAccess baseTypeAccess) {
		final UnknownTypeAccess fieldTypeAccess = getFieldType().makeTypeAccess();
		fieldTypeAccess.addArgument(baseTypeAccess);

		return fieldTypeAccess;
	}

	public static BoundedBindingBuilder getReturnBindingBuilder() {
		return new BoundedBindingBuilder().addName("~ret");
	}

	public static ValueReference getReturnValueReference() {
		return ValueReference.of("~ret");
	}

	public static Label makeLabel(final int index) {
		return new Label("label" + index);
	}

	public static ValueReference makeValueReference(final int index) {
		return ValueReference.of("~sym" + index);
	}

	public static FrameCondition makeHeapFrameCondition() {
		return new FrameCondition(false, new List<ValueReference>(ValueReference.of("~heap")));
	}

	public static FunctionReference getIntCaster() {
		return loadProgram().lookupFunction("~int")
				.orElseThrow(() -> new IllegalStateException("Missing definition for ~int casting function"))
				.makeFunctionReference();
	}

	public static FunctionReference getCmpReference() {
		return loadProgram().lookupFunction("~cmp")
				.orElseThrow(() -> new IllegalStateException("Missing definition for ~cmp")).makeFunctionReference();
	}

	public static void inject(final Program program) {
		loadProgram().inject(program);
	}

}
