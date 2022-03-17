package byteback.core.converter.soot.boogie;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import beaver.Parser;
import byteback.frontend.boogie.ast.Program;
import byteback.frontend.boogie.util.ParserUtil;

public class BoogiePreamble {

    static private final Logger log = LoggerFactory.getLogger(BoogiePreamble.class);

    static public Optional<Program> preamble = Optional.empty();

    static public Program load() {
        return preamble.orElseGet(BoogiePreamble::initialize);
    }

    static public Program initialize() {
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();
        final InputStream stream = loader.getResourceAsStream("boogie/BytebackPrelude.bpl");
        final Reader reader = new InputStreamReader(stream);

        try {
            final Program program = ParserUtil.parseBoogieProgram(reader);
            preamble = Optional.of(program);

            return program;
        } catch (final IOException exception) {
            log.error("Exception while opening the preamble");
            throw new RuntimeException(exception);
        } catch (final Parser.Exception exception) {
            log.error("Exception while parsing the preamble");
            throw new RuntimeException(exception);
        }
    }

}
