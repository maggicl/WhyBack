package byteback.mlcfg.syntax;

import byteback.frontend.boogie.ast.Printable;
import byteback.mlcfg.printer.Statement;
import static byteback.mlcfg.printer.Statement.line;
import static byteback.mlcfg.printer.Statement.many;
import static byteback.mlcfg.printer.Statement.indent;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public record WhyProgram(Statement program) implements Printable {
	private static final String IMPORT_FRAGMENT_PATH = "fragment/import.mlw";

	private static Statement getImports() {
		final ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		try (final InputStream is = classloader.getResourceAsStream(IMPORT_FRAGMENT_PATH)) {
			if (is == null) {
				throw new IllegalStateException("Could not read import fragment file: input stream is null");
			}
			final List<Statement> lines = new ArrayList<>();
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
		b.append(Statement.block(
				line("module Program"),
				getImports(),
				indent(line(""), program),
				line("end")
		));
	}
}