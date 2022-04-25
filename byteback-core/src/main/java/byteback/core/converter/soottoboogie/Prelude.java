package byteback.core.converter.soottoboogie;

import beaver.Parser;
import byteback.core.Configuration;
import byteback.core.util.Lazy;
import byteback.frontend.boogie.ast.*;
import byteback.frontend.boogie.builder.BoundedBindingBuilder;
import byteback.frontend.boogie.util.ParserUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Facade class interfacing to the prelude file.
 *
 * @author paganma
 */
public class Prelude {

	private static final Logger log = LoggerFactory.getLogger(Prelude.class);

	/**
	 * Path to the prelude file included in the resources.
	 */
	private static final String PRELUDE_PATH = "boogie/BytebackPrelude.bpl";

	/**
	 * Stream from which the prelude will be loaded.
	 */
	private static InputStream stream;

	/**
	 * Lazily initialized prelude program.
	 */
	private static final Lazy<Program> PRELUDE_PROGRAM = Lazy.from(Prelude::initializeProgram);

	/**
	 * Configures the choice of prelude file.
	 *
	 * @param configuration
	 *            A {@link Configuration} instance that may include the
	 *            {@code --prelude} option specified by the user.
	 */
	public static void configure(final Configuration configuration) {
		final String path = configuration.getPreludePath();

		if (path != null) {
			final File file = new File(path);

			try {
				stream = new FileInputStream(file);
			} catch (final FileNotFoundException exception) {
				throw new RuntimeException("Failed to find user-provided prelude file", exception);
			}
		}

	}

	/**
	 * Lazily loads the prelude on request.
	 *
	 * @return The prelude {@link Program}.
	 */
	public static Program loadProgram() {
		return PRELUDE_PROGRAM.get();
	}

	/**
	 * Parses and loads the prelude.
	 *
	 * @return The prelude {@link Program}.
	 */
	public static Program initializeProgram() {
		if (stream == null) {
			final ClassLoader loader = Thread.currentThread().getContextClassLoader();
			stream = loader.getResourceAsStream(PRELUDE_PATH);
			assert stream != null;
		}

		final var reader = new InputStreamReader(stream);

		try {
			return ParserUtil.parseBoogieProgram(reader);
		} catch (final IOException exception) {
			throw new RuntimeException("Failed to open the preamble ", exception);
		} catch (final Parser.Exception exception) {
			throw new RuntimeException("Failed to ", exception);
		}
	}

	/**
	 * Getter for the reference-type model.
	 *
	 * @return The {@code Reference} {@link Type}.
	 */
	public static Type getReferenceType() {
		final TypeDefinition typeDefinition = loadProgram().lookupTypeDefinition("Reference")
				.orElseThrow(() -> new IllegalStateException("Missing definition for Reference type"));

		return typeDefinition.getType();
	}

	/**
	 * Getter for the heap-type model.
	 *
	 * @return The {@code Store} {@link Type}.
	 */
	public static Type getHeapType() {
		final TypeDefinition typeDefinition = loadProgram().lookupTypeDefinition("Store")
				.orElseThrow(() -> new IllegalStateException("Missing definition for Store type"));

		return typeDefinition.getType();
	}

	/**
	 * Getter for the field-type model.
	 *
	 * @return The {@code Field} {@link Type}.
	 */
	public static DefinedType getFieldType() {
		final TypeDefinition typeDefinition = loadProgram().lookupTypeDefinition("Field")
				.orElseThrow(() -> new IllegalStateException("Missing definition for Field type"));

		return typeDefinition.getType();
	}

	/**
	 * Getter for the Boogie boolean type.
	 *
	 * @return The {@code bool} {@link Type}.
	 */
	public static Type getBooleanType() {
		return BooleanType.instance();
	}

	/**
	 * Getter for the Boogie integer type.
	 *
	 * @return The {@code int} {@link Type}.
	 */
	public static Type getIntegerType() {
		return IntegerType.instance();
	}

	/**
	 * Getter for the Boogie real type.
	 *
	 * @return The {@code real} {@link Type}.
	 */
	public static Type getRealType() {
		return RealType.instance();
	}

	/**
	 * Getter for the global variable representing the heap.
	 *
	 * @return The {@code ~heap} variable of type {@code Store}.
	 */
	public static Variable getHeapVariable() {
		return loadProgram().lookupLocalVariable("~heap")
				.orElseThrow(() -> new IllegalStateException("Missing definition for the ~heap function"));
	}

	/**
	 * Getter for the global variable representing the heap.
	 *
	 * @return The {@code ~heap} variable of type {@code Store}.
	 */
	public static Variable getNullConstant() {
		return loadProgram().lookupLocalVariable("~null")
				.orElseThrow(() -> new IllegalStateException("Missing definition for the ~null function"));
	}

	/**
	 * Getter for the heap-read function.
	 *
	 * @return The {@code ~read} function.
	 */
	public static Function getHeapAccessFunction() {
		return loadProgram().lookupFunction("~read")
				.orElseThrow(() -> new IllegalStateException("Missing definition for the ~read function"));
	}

	/**
	 * Getter for the heap-update function.
	 *
	 * @return The {@code ~update} function.
	 */
	public static Function getHeapUpdateFunction() {
		return loadProgram().lookupFunction("~update")
				.orElseThrow(() -> new IllegalStateException("Missing definition for the ~update function"));
	}

	/**
	 * Getter for the "new" procedure, used to instantiate new references.
	 *
	 * @return The {@code ~new} procedure.
	 */
	public static Procedure getNewProcedure() {
		return loadProgram().lookupProcedure("~new")
				.orElseThrow(() -> new IllegalStateException("Missing definition for the ~new procedure"));
	}

	/**
	 * Getter for the "new" procedure, used to instantiate new arrays.
	 *
	 * @return The {@code ~array} procedure.
	 */
	public static Procedure getArrayProcedure() {
		return loadProgram().lookupProcedure("~array")
				.orElseThrow(() -> new IllegalStateException("Missing definition for the ~array procedure"));
	}

	/**
	 * Getter for the array-length function.
	 *
	 * @return The {@code ~lengthof} function.
	 */
	public static Function getArrayLengthFunction() {
		return loadProgram().lookupFunction("~lengthof")
				.orElseThrow(() -> new IllegalStateException("Missing definition for the ~lengthof function"));
	}

	/**
	 * Getter for the array-access function.
	 *
	 * @return The {@code ~get} function.
	 */
	public static Function getArrayAccessFunction() {
		return loadProgram().lookupFunction("~get")
				.orElseThrow(() -> new IllegalStateException("Missing definition for the ~getfunction"));
	}

	/**
	 * Getter for the array-update function.
	 *
	 * @return The {@code ~insert} function.
	 */
	public static Function getArrayUpdateFunction() {
		return loadProgram().lookupFunction("~insert")
				.orElseThrow(() -> new IllegalStateException("Missing definition for the ~insert"));
	}

	/**
	 * Getter for the int-caster.
	 *
	 * @return The {@code ~int} function.
	 */
	public static Function getIntCastingFunction() {
		return loadProgram().lookupFunction("~int")
				.orElseThrow(() -> new IllegalStateException("Missing definition for ~int casting function"));
	}

	/**
	 * Getter for the CMP operator.
	 *
	 * @return The {@code ~cmp} function.
	 */
	public static Function getCmpFunction() {
		return loadProgram().lookupFunction("~cmp")
				.orElseThrow(() -> new IllegalStateException("Missing definition for ~cmp"));
	}

	/**
	 * Getter for the array field constant of a specified type.
	 *
	 * @param typeAccess
	 *            The type of the array field.
	 * @return The {@code ~Array.*} field variable.
	 */
	public static Variable getArrayTypeVariable(final TypeAccess typeAccess) {
		final String arrayIdentifier = "~Array." + typeAccess.getIdentifier();
		return loadProgram().lookupLocalVariable(arrayIdentifier).orElseThrow(
				() -> new IllegalStateException("Missing definition for array variable " + arrayIdentifier));
	}

	/**
	 * Builder for a length-access expression.
	 *
	 * @param array
	 *            The array to be accessed.
	 * @return The {@link Expression} accessing the length of the array.
	 */
	public static Expression getLengthAccessExpression(final Expression array) {
		final FunctionReference reference = getArrayLengthFunction().makeFunctionReference();
		reference.addArgument(getHeapVariable().makeValueReference());
		reference.addArgument(array);

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
	public static Expression getHeapAccessExpression(final Expression base, final Expression field) {
		final FunctionReference reference = getHeapAccessFunction().makeFunctionReference();
		reference.addArgument(getHeapVariable().makeValueReference());
		reference.addArgument(base);
		reference.addArgument(field);

		return reference;
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

	/**
	 * Builder for a heap-update expression.
	 *
	 * @param base
	 *            The base reference from which the field is being updated.
	 * @param field
	 *            The field that is being updated.
	 * @return The {@link Expression} updating the field.
	 */
	public static Statement getHeapUpdateStatement(final Expression base, final Expression field,
			final Expression value) {
		final FunctionReference updateReference = getHeapUpdateFunction().makeFunctionReference();
		final ValueReference heapReference = getHeapVariable().makeValueReference();
		final Assignee heapAssignee = Assignee.of(heapReference);
		updateReference.addArgument(heapReference);
		updateReference.addArgument(base);
		updateReference.addArgument(field);
		updateReference.addArgument(value);

		return new AssignmentStatement(heapAssignee, updateReference);
	}

	/**
	 * Builder for an array-update expression.
	 *
	 * @param base
	 *            The base reference from which the field is being updated.
	 * @param field
	 *            The field that is being updated.
	 * @return The {@link Expression} updating the field.
	 */
	public static Statement getArrayUpdateStatement(final TypeAccess typeAccess, final Expression base,
			final Expression index, final Expression value) {
		final FunctionReference updateReference = getArrayUpdateFunction().makeFunctionReference();
		final ValueReference heapReference = getHeapVariable().makeValueReference();
		final Variable arrayType = getArrayTypeVariable(typeAccess);
		final Assignee heapAssignee = Assignee.of(heapReference);
		updateReference.addArgument(heapReference);
		updateReference.addArgument(base);
		updateReference.addArgument(arrayType.makeValueReference());
		updateReference.addArgument(index);
		updateReference.addArgument(value);

		return new AssignmentStatement(heapAssignee, updateReference);
	}

	/**
	 * Builder for a field type-access.
	 *
	 * @param typeAccess
	 *            The base type of the field.
	 * @return The {@link TypeAccess} targeting the type of the field.
	 */
	public static TypeAccess getFieldTypeAccess(final TypeAccess typeAccess) {
		final UnknownTypeAccess fieldTypeAccess = getFieldType().makeTypeAccess();
		fieldTypeAccess.addArgument(typeAccess);

		return fieldTypeAccess;
	}

	/**
	 * Builder for the procedure's return binding.
	 *
	 * @param typeAccess
	 *            The type of the return binding.
	 * @return The {@code ~ret} {@link BoundedBinding}.
	 */
	public static BoundedBinding getReturnBinding(final TypeAccess typeAccess) {
		return new BoundedBindingBuilder().addName("~ret").typeAccess(typeAccess).build();
	}

	/**
	 * Getter for the procedure's return reference.
	 *
	 * @return The {@code ~ret} {@link ValueReference}.
	 */
	public static ValueReference getReturnValueReference() {
		return ValueReference.of("~ret");
	}

	/**
	 * Creates a new label from its index.
	 *
	 * @param index
	 *            The index of the label.
	 * @return The new {@link Label} statement.
	 */
	public static Label makeLabel(final int index) {
		return new Label("label" + index);
	}

	/**
	 * Creates a new temporary value reference from a unique index.
	 *
	 * @param index
	 *            The index of the temporary variable.
	 * @return The new {@link ValueReference} to the temporary variable.
	 */
	public static ValueReference makeValueReference(final int index) {
		return ValueReference.of("~sym" + index);
	}

	/**
	 * Injects the prelude into a given program.
	 *
	 * @param program
	 *            The program to be injected.
	 */
	public static void inject(final Program program) {
		loadProgram().inject(program);
	}

}
