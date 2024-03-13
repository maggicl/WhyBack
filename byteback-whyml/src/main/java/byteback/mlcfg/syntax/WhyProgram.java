package byteback.mlcfg.syntax;

import byteback.frontend.boogie.ast.Printable;
import byteback.mlcfg.Utils;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public record WhyProgram(String program) implements Printable {
	public static final String INDENT = "  ";
	private static final String IMPORT_FRAGMENT_PATH = "fragment/import.mlw";

	private static String getImports() {
		final ClassLoader classloader = Thread.currentThread().getContextClassLoader();
		try (final InputStream is = classloader.getResourceAsStream(IMPORT_FRAGMENT_PATH)) {
			if (is == null) {
				throw new IllegalStateException("Could not read import fragment file: input stream is null");
			}
			try (Scanner it = new Scanner(is, StandardCharsets.UTF_8).useDelimiter("\n")) {
				final StringBuilder sb = new StringBuilder();
				while (it.hasNext()) {
					sb.append(INDENT).append(it);
				}
				sb.append("\n");
				return sb.toString();
			}
		} catch (IOException e) {
			throw new IllegalStateException("Could not read import fragment file", e);
		}
	}

	@Override
	public void print(StringBuilder b) {
		b.append("module Program\n")
				.append(getImports())
				.append('\n')
				.append(Utils.indent(program, 1))
				.append("end\n");
	}
}