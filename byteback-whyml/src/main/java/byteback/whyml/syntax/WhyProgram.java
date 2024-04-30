package byteback.whyml.syntax;

import byteback.frontend.boogie.ast.Printable;
import byteback.whyml.printer.Code;
import static byteback.whyml.printer.Code.line;
import static byteback.whyml.printer.Code.many;
import static byteback.whyml.printer.Code.indent;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public record WhyProgram(Code program) implements Printable {
	private static final String IMPORT_FRAGMENT_PATH = "fragment/import.mlw";

	private static Code getImports() {
		final ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		try (final InputStream is = classloader.getResourceAsStream(IMPORT_FRAGMENT_PATH)) {
			if (is == null) {
				throw new IllegalStateException("Could not read import fragment file: input stream is null");
			}
			final List<Code> lines = new ArrayList<>();
			try (Scanner it = new Scanner(is, StandardCharsets.UTF_8).useDelimiter("\n")) {
				while (it.hasNext()) {
					lines.add(line(it.next()));
				}
			}
			return many(lines.stream());
		} catch (IOException e) {
			throw new IllegalStateException("Could not read import fragment file", e);
		}
	}

	@Override
	public void print(StringBuilder b) {
		b.append(Code.block(
				line("module Program"),
				getImports(),
				indent(line(""), program),
				line("end")
		));
	}
}