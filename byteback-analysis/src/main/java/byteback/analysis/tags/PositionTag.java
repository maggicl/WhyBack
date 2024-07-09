package byteback.analysis.tags;

import soot.tagkit.Tag;

public class PositionTag implements Tag {

	public final String file;

	public final int lineNumber;
	public final int startColumn;
	public final int endColumn;

	public PositionTag(final String file, final int lineNumber, int startColumn, int endColumn) {
		this.file = file;
		this.lineNumber = lineNumber;
		this.startColumn = startColumn;
		this.endColumn = endColumn;
	}

	@Override
	public String getName() {
		return "PositionTag";
	}

	@Override
	public byte[] getValue() {
		return (file + ":" + lineNumber).getBytes();
	}

}
