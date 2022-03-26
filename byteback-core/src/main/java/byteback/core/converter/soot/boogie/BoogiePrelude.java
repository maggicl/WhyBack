package byteback.core.converter.soot.boogie;

import beaver.Parser;
import byteback.core.util.Lazy;
import byteback.frontend.boogie.ast.Accessor;
import byteback.frontend.boogie.ast.BooleanType;
import byteback.frontend.boogie.ast.DefinedType;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.Function;
import byteback.frontend.boogie.ast.FunctionReference;
import byteback.frontend.boogie.ast.IntegerType;
import byteback.frontend.boogie.ast.Label;
import byteback.frontend.boogie.ast.PrintUtil;
import byteback.frontend.boogie.ast.Program;
import byteback.frontend.boogie.ast.RealType;
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
import java.io.Reader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BoogiePrelude {

	private static final Logger log = LoggerFactory.getLogger(BoogiePrelude.class);

	private static final Lazy<Program> preamble = Lazy.from(BoogiePrelude::initializeProgram);

	public static Program loadProgram() {
		return preamble.get();
	}

	public static Program initializeProgram() {
		final ClassLoader loader = Thread.currentThread().getContextClassLoader();
		final InputStream stream = loader.getResourceAsStream("boogie/BytebackPrelude.bpl");
		assert stream != null;
		final Reader reader = new InputStreamReader(stream);

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
		final TypeDefinition typeDefinition = loadProgram().lookupTypeDefinition("Reference").orElseThrow(() -> {
			throw new IllegalStateException("Missing definition for Reference type");
		});

		return typeDefinition.getType();
	}

	public static Type getHeapType() {
		final TypeDefinition typeDefinition = loadProgram().lookupTypeDefinition("Store").orElseThrow(() -> {
			throw new IllegalStateException("Missing definition for heap Store type");
		});

		return typeDefinition.getType();
	}

	public static DefinedType getFieldType() {
		final TypeDefinition typeDefinition = loadProgram().lookupTypeDefinition("Field").orElseThrow(() -> {
			throw new IllegalStateException("Missing definition for Field type");
		});

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
		return loadProgram().lookupVariable("~heap").orElseThrow(() -> {
			throw new IllegalStateException("Missing definition for the ~heap variable");
		});
	}

	public static Function getHeapAccessFunction() {
		return loadProgram().lookupFunction("~read").orElseThrow(() -> {
			throw new IllegalStateException("Missing definition for the ~read variable");
		});
	}

	public static Expression getHeapAccessExpression(final Expression base, final Expression field) {
		final FunctionReference reference = getHeapAccessFunction().getFunctionReference();
		reference.addArgument(getHeapVariable().getValueReference());
		reference.addArgument(base);
		reference.addArgument(field);

		return reference;
	}

	public static TypeAccess getFieldTypeAccess(final TypeAccess baseTypeAccess) {
		final UnknownTypeAccess fieldTypeAccess = getFieldType().getTypeAccess();
		fieldTypeAccess.addArgument(baseTypeAccess);

		return fieldTypeAccess;
	}

	public static FunctionReference getOperator(final String name, final TypeAccess typeAccess) {
		final String typeName = PrintUtil.toString(typeAccess);

		return loadProgram().lookupFunction(name + "_" + typeName).orElseThrow(() ->
			new IllegalStateException("Missing definition for the " + name + " operator of type " + typeName)
		).getFunctionReference();
	}

	public static BoundedBindingBuilder getReturnBindingBuilder() {
		return new BoundedBindingBuilder().addName("~ret");
	}

	public static ValueReference getReturnValueReference() {
		return new ValueReference(new Accessor("~ret"));
	}

	public static Label getLabel(final int index) {
		return new Label("label" + index);
	}

  public static FunctionReference getIntCaster() {
    return loadProgram().lookupFunction("~int").orElseThrow(() -> new IllegalStateException("Missing definition for integercasting function")).getFunctionReference();
  }

}
