package byteback.core.converter.soottoboogie;

import beaver.Parser;
import byteback.core.Configuration;
import byteback.frontend.boogie.ast.*;
import byteback.frontend.boogie.util.ParserUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;

/**
 * Facade class interfacing to the prelude file.
 *
 * @author paganma
 */
public class Prelude {

	private static final String PRELUDE_PATH = "boogie/BytebackPrelude.bpl";

	private static Prelude instance = new Prelude();

	public static Prelude instance() {
		return instance;
	}

	private Program program;

	private Prelude() {
	}

	/**
	 * Parses and loads the prelude.
	 *
	 * @param stream
	 *            The stream from which the prelude will be parsed.
	 * @return The prelude {@link Program}.
	 */
	private void initializeProgram(final InputStream stream) {
		final var reader = new InputStreamReader(stream);

		try {
			program = ParserUtil.parseBoogieProgram(reader);
		} catch (final IOException exception) {
			throw new RuntimeException("Failed to open the preamble ", exception);
		} catch (final Parser.Exception exception) {
			throw new RuntimeException("Failed to ", exception);
		}
	}

	/**
	 * Loads the default prelude program from the resources.
	 */
	public void loadDefault() {
		final ClassLoader loader = Thread.currentThread().getContextClassLoader();
		final InputStream stream = loader.getResourceAsStream(PRELUDE_PATH);
		assert stream != null;
		initializeProgram(stream);
	}

	/**
	 * Loads the prelude program from an external file.
	 *
	 * @param path
	 *            Path to the prelude file.
	 */
	public void loadFile(final Path path) {
		final File file = path.toFile();

		try {
			final InputStream stream = new FileInputStream(file);
			initializeProgram(stream);
		} catch (final IOException exception) {
			throw new RuntimeException("Failed to find user-provided prelude file", exception);
		}
	}

	/**
	 * Configures the choice of prelude file.
	 *
	 * @param configuration
	 *            A {@link Configuration} instance that may include the
	 *            {@code --prelude} option specified by the user.
	 */
	public void configure(final Configuration configuration) {
		final Path path = configuration.getPreludePath();

		if (path != null) {
			loadFile(path);
		} else {
			loadDefault();
		}
	}

	/**
	 * Returns the prelude program, if loaded.
	 *
	 * @return The prelude {@link Program}.
	 * @throws IllegalStateException
	 *             If the prelude program was not loaded.
	 */
	public Program program() {
		if (program == null) {
			throw new IllegalStateException("Prelude program is not loaded");
		} else {
			return program;
		}
	}

	/**
	 * Getter for the heap variable.
	 *
	 * @return The {@code ~heap} {@link Variable}.
	 */
	public Variable getHeapVariable() {
		final Variable variable = program().lookupLocalVariable("~heap")
				.orElseThrow(() -> new IllegalStateException("Missing definition for the ~heap variable"));

		return variable;
	}

	public Variable getPrimitiveTypeConstant() {
		final Variable variable = program().lookupLocalVariable("~Primitive")
				.orElseThrow(() -> new IllegalStateException("Missing definition for the ~Primitive variable"));

		return variable;
	}

	/**
	 * Getter for the reference type.
	 *
	 * @return The {@code Reference} {@link Type}.
	 */
	public Type getReferenceType() {
		final TypeDefinition typeDefinition = program().lookupTypeDefinition("Reference")
				.orElseThrow(() -> new IllegalStateException("Missing definition for Reference type"));

		return typeDefinition.getType();
	}

	/**
	 * Getter for the field type.
	 *
	 * @return The definition of the {@code Field} {@link Type}.
	 */
	public DefinedType getFieldType() {
		final TypeDefinition typeDefinition = program().lookupTypeDefinition("Field")
				.orElseThrow(() -> new IllegalStateException("Missing definition for Field type"));

		return typeDefinition.getType();
	}

	/**
	 * Getter for the reference-type type.
	 *
	 * @return The {@link Type} corresponding to a generic reference type.
	 */
	public DefinedType getTypeType() {
		final TypeDefinition typeDefinition = program().lookupTypeDefinition("Type")
				.orElseThrow(() -> new IllegalStateException("Missing definition for Type type"));

		return typeDefinition.getType();
	}

	/**
	 * Getter for the Boogie boolean type.
	 *
	 * @return The {@code bool} {@link Type}.
	 */
	public Type getBooleanType() {
		return BooleanType.instance();
	}

	/**
	 * Getter for the Boogie integer type.
	 *
	 * @return The {@code int} {@link Type}.
	 */
	public Type getIntegerType() {
		return IntegerType.instance();
	}

	/**
	 * Getter for the Boogie real type.
	 *
	 * @return The {@code real} {@link Type}.
	 */
	public Type getRealType() {
		return RealType.instance();
	}

	/**
	 * Getter for the global variable representing the heap.
	 *
	 * @return The {@code ~heap} variable of type {@code Store}.
	 */
	public Variable getNullConstant() {
		return program().lookupLocalVariable("~null")
				.orElseThrow(() -> new IllegalStateException("Missing definition for the ~null function"));
	}

	/**
	 * Getter for the heap-read function.
	 *
	 * @return The {@code ~heap.read} function.
	 */
	public Function getHeapAccessFunction() {
		return program().lookupFunction("~heap.read")
				.orElseThrow(() -> new IllegalStateException("Missing definition for the ~heap.read function"));
	}

	public Function getTypeReferenceFunction() {
		return program().lookupFunction("~type.reference")
				.orElseThrow(() -> new IllegalStateException("Missing definition for the ~type.reference function"));
	}

	public Function getTypeOfFunction() {
		return program().lookupFunction("~typeof")
				.orElseThrow(() -> new IllegalStateException("Missing definition for the ~typeof function"));
	}

	public Function getArrayTypeFunction() {
		return program().lookupFunction("~array.type")
				.orElseThrow(() -> new IllegalStateException("Missing definition for the ~heap.type function"));
	}

	/**
	 * Getter for the heap-update function.
	 *
	 * @return The {@code ~heap.update} function.
	 */
	public Function getHeapUpdateFunction() {
		return program().lookupFunction("~heap.update")
				.orElseThrow(() -> new IllegalStateException("Missing definition for the ~heap.update function"));
	}

	/**
	 * Getter for the "new" procedure, used to instantiate new references.
	 *
	 * @return The {@code ~new} procedure.
	 */
	public Procedure getNewProcedure() {
		return program().lookupProcedure("~new")
				.orElseThrow(() -> new IllegalStateException("Missing definition for the ~new procedure"));
	}

	/**
	 * Getter for the instanceof function.
	 *
	 * @return The {@code ~instanceof} function.
	 */
	public Function getTypeCheckFunction() {
		return program().lookupFunction("~instanceof")
				.orElseThrow(() -> new IllegalStateException("Missing definition for the ~instanceof function"));
	}

	/**
	 * Getter for the "new" procedure, used to instantiate new arrays.
	 *
	 * @return The {@code ~array} procedure.
	 */
	public Procedure getArrayProcedure() {
		return program().lookupProcedure("~array")
				.orElseThrow(() -> new IllegalStateException("Missing definition for the ~array procedure"));
	}

	/**
	 * Getter for the array-length function.
	 *
	 * @return The {@code ~lengthof} function.
	 */
	public Function getArrayLengthFunction() {
		return program().lookupFunction("~lengthof")
				.orElseThrow(() -> new IllegalStateException("Missing definition for the ~lengthof function"));
	}

	/**
	 * Getter for the array-update function.
	 *
	 * @return The {@code ~insert} function.
	 */
	public Function getArrayUpdateFunction() {
		return program().lookupFunction("~insert")
				.orElseThrow(() -> new IllegalStateException("Missing definition for the ~insert function"));
	}

	/**
	 * Getter for the int-caster.
	 *
	 * @return The {@code ~int} function.
	 */
	public Function getIntCastingFunction() {
		return program().lookupFunction("~int")
				.orElseThrow(() -> new IllegalStateException("Missing definition for ~int casting function"));
	}

	/**
	 * Getter for the CMP operator.
	 *
	 * @return The {@code ~cmp} function.
	 */
	public Function getCmpFunction() {
		return program().lookupFunction("~cmp")
				.orElseThrow(() -> new IllegalStateException("Missing definition for ~cmp"));
	}

	public Function getBoxFunction() {
		return program().lookupFunction("~box")
				.orElseThrow(() -> new IllegalStateException("Missing definition for ~box"));
	}

	public Function getUnboxFunction() {
		return program().lookupFunction("~unbox")
				.orElseThrow(() -> new IllegalStateException("Missing definition for ~unbox"));
	}

	public Function getElementFunction() {
		return program().lookupFunction("~element")
				.orElseThrow(() -> new IllegalStateException("Missing definition for ~element"));
	}

	/**
	 * Builder for a length-access expression.
	 *
	 * @param array
	 *            The array to be accessed.
	 * @return The {@link Expression} accessing the length of the array.
	 */
	public Expression getLengthAccessExpression(final Expression array) {
		final FunctionReference reference = getArrayLengthFunction().makeFunctionReference();
		reference.addArgument(array);

		return reference;
	}

	/**
	 * Builder for a type-check expression.
	 *
	 * @param reference
	 *            The reference to the instance.
	 * @param type
	 *            The reference to the type to be checked.
	 * @return The {@link Expression} accessing the length of the array.
	 */
	public Expression makeTypeCheckExpression(final Expression instance, final Expression type) {
		final FunctionReference reference = getTypeCheckFunction().makeFunctionReference();
		reference.addArgument(instance);
		reference.addArgument(type);

		return reference;
	}

	/**
	 * Builder for a heap-access expression.
	 *
	 * @param base
	 *            The base reference from which the field is being accessed.
	 * @param field
	 *            The field that is being accessed.
	 * @return The heap access {@link Expression}.
	 */
	public Expression makeHeapAccessExpression(final Expression base, final Expression field) {
		final ValueReference heapReference = getHeapVariable().makeValueReference();
		final FunctionReference accessReference = getHeapAccessFunction().makeFunctionReference();
		accessReference.addArgument(heapReference);
		accessReference.addArgument(base);
		accessReference.addArgument(field);

		return accessReference;
	}

	public Expression makeStaticAccessExpression(final Expression base, final Expression field) {
		final FunctionReference referenceReference = getTypeReferenceFunction().makeFunctionReference();
		referenceReference.addArgument(base);

		return makeHeapAccessExpression(referenceReference, field);
	}

	/**
	 * Builder for an array-access expression.
	 *
	 * @param typeAccess
	 *            The type of the array being accessed.
	 * @param base
	 *            The array being accessed.
	 * @param index
	 *            The index of the array being accessed.
	 * @return The {@link Expression} accessing the given array at the given index.
	 */
	public Expression makeArrayAccessExpression(final TypeAccess typeAccess, final Expression base,
			final Expression index) {
		final var coercion = new CoercionOperation();
		final FunctionReference elementReference = getElementFunction().makeFunctionReference();
		final FunctionReference unboxReference = getUnboxFunction().makeFunctionReference();
		elementReference.addArgument(index);
		unboxReference.addArgument(makeHeapAccessExpression(base, elementReference));
		coercion.setOperand(unboxReference);
		coercion.setTarget(typeAccess);

		return coercion;
	}

	/**
	 * Builder for a heap-update statement.
	 *
	 * @param base
	 *            The base reference from which the field is being updated.
	 * @param field
	 *            The field that is being updated.
	 * @return The {@link Expression} updating the field.
	 */
	public Statement makeHeapUpdateStatement(final Expression base, final Expression field, final Expression value) {
		final ValueReference heapReference = getHeapVariable().makeValueReference();
		final FunctionReference updateReference = getHeapUpdateFunction().makeFunctionReference();
		updateReference.addArgument(heapReference);
		updateReference.addArgument(base);
		updateReference.addArgument(field);
		updateReference.addArgument(value);

		return new AssignmentStatement(new Assignee(heapReference.getAccessor()), updateReference);
	}

	public Statement makeStaticUpdateStatement(final Expression base, final Expression field, final Expression value) {
		final FunctionReference referenceReference = getTypeReferenceFunction().makeFunctionReference();
		referenceReference.addArgument(base);

		return makeHeapUpdateStatement(referenceReference, field, value);
	}

	/**
	 * Builder for an array-update statement.
	 *
	 * @param base
	 *            The base reference from which the field is being updated.
	 * @param field
	 *            The field that is being updated.
	 * @return The {@link Expression} updating the field.
	 */
	public Statement makeArrayUpdateStatement(final TypeAccess typeAccess, final Expression base,
			final Expression index, final Expression value) {
		final FunctionReference elementReference = getElementFunction().makeFunctionReference();
		final FunctionReference boxReference = getBoxFunction().makeFunctionReference();
		elementReference.addArgument(index);
		boxReference.addArgument(value);

		return makeHeapUpdateStatement(base, elementReference, boxReference);
	}

	/**
	 * Builder for a field type-access.
	 *
	 * @param typeAccess
	 *            The base type of the field.
	 * @return The {@link TypeAccess} targeting the type of the field.
	 */
	public TypeAccess makeFieldTypeAccess(final TypeAccess typeAccess) {
		final UnknownTypeAccess fieldTypeAccess = getFieldType().makeTypeAccess();
		fieldTypeAccess.addArgument(typeAccess);

		return fieldTypeAccess;
	}

	/**
	 * Injects the declarations of the loaded prelude in-place.
	 *
	 * @param program
	 *            The program to be injected
	 */
	public void inject(final Program program) {
		program().inject(program);
	}

}
