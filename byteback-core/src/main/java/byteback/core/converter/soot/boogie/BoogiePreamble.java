package byteback.core.converter.soot.boogie;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaver.Parser;
import byteback.core.util.Lazy;
import byteback.frontend.boogie.ast.Program;
import byteback.frontend.boogie.ast.Type;
import byteback.frontend.boogie.ast.TypeDefinition;
import byteback.frontend.boogie.util.ParserUtil;

public class BoogiePreamble {

    static private final Logger log = LoggerFactory.getLogger(BoogiePreamble.class);

    static private final Lazy<Program> preamble = Lazy.from(BoogiePreamble::initializeProgram);

    static private final Lazy<Type> referenceType = Lazy.from(BoogiePreamble::initializeReferenceType);

    static private final Lazy<Type> heapType = Lazy.from(BoogiePreamble::initializeHeapType);

    static public Program loadProgram() {
        return preamble.get();
    }

    static public Program initializeProgram() {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        final InputStream stream = loader.getResourceAsStream("boogie/BytebackPrelude.bpl");
        final Reader reader = new InputStreamReader(stream);

        try {
            final Program program = ParserUtil.parseBoogieProgram(reader);
            return program;
        } catch (final IOException exception) {
            log.error("Exception while opening the preamble");
            throw new RuntimeException(exception);
        } catch (final Parser.Exception exception) {
            log.error("Exception while parsing the preamble");
            throw new RuntimeException(exception);
        }
    }

    static public Type getReferenceType() {
        return referenceType.get();
    }

    static public Type initializeReferenceType() {
        final TypeDefinition typeDefinition = loadProgram().lookupTypeDefinition("Reference").orElseThrow(() -> {
            throw new IllegalStateException("Missing definition for Reference type");
        });

        return typeDefinition.getType();
    }

    static public Type getHeapType() {
        return heapType.get();
    }

    static public Type initializeHeapType() {
        final TypeDefinition typeDefinition = loadProgram().lookupTypeDefinition("Store").orElseThrow(() -> {
            throw new IllegalStateException("Missing definition for heap Store type");
        });

        return typeDefinition.getType();
    }

}
