package byteback.annotations;

import java.io.FileReader;

import byteback.annotations.Contract.Attach;
import byteback.annotations.Contract.Ensure;
import byteback.annotations.Contract.Predicate;
import byteback.annotations.Contract.Pure;
import byteback.annotations.Contract.Return;

@Attach(FileReader.class)
public abstract class FileReaderSpec {

	@Return
	public FileReaderSpec() {
	}

	@Pure
	public abstract boolean isClosed();

	@Predicate
	public boolean is_closed() {
		return isClosed();
	}

	@Ensure("is_closed")
	public void close() {
	}

}
