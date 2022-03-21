package byteback.core.converter.soot.boogie;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaver.Parser;
import byteback.core.util.Lazy;
import byteback.frontend.boogie.ast.BooleanType;
import byteback.frontend.boogie.ast.Expression;
import byteback.frontend.boogie.ast.Function;
import byteback.frontend.boogie.ast.FunctionReference;
import byteback.frontend.boogie.ast.IntegerType;
import byteback.frontend.boogie.ast.PrintUtil;
import byteback.frontend.boogie.ast.Program;
import byteback.frontend.boogie.ast.RealType;
import byteback.frontend.boogie.ast.Type;
import byteback.frontend.boogie.ast.TypeDefinition;
import byteback.frontend.boogie.ast.Variable;
import byteback.frontend.boogie.util.ParserUtil;

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

    public static Expression getHeapAccessExpression(Expression base, Expression field) {
        final FunctionReference reference = getHeapAccessFunction().getFunctionReference();
        reference.addArgument(getHeapVariable().getValueReference());
        reference.addArgument(base);
        reference.addArgument(field);

        return reference;
    }

}
